package examples.ch09.productsync;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import examples.ch09.shared.Money;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** The adapters against a real (temporary) file system. */
class FileAdaptersTest {
  @TempDir
  Path dir;

  @Test
  void theImportProcessSeesTheCsvFileOnlyWhenItIsComplete() throws IOException {
    ImportFile file = new CsvImportFiles(dir)
        .create("R42", Instant.parse("2026-10-12T02:00:00Z"));
    file.add(new ImportLine("R42", "TS-100", "Tee, \"basic\", white",
        Money.of("9.90", "USD"), "R42/TS-100/IMG-1.jpg"));
    Path csv = dir.resolve("R42-20261012T020000Z.csv");

    assertFalse(Files.exists(csv));

    file.publish();

    assertEquals(List.of(CsvImportFiles.HEADER,
        "R42,TS-100,\"Tee, \"\"basic\"\", white\",9.90,USD,R42/TS-100/IMG-1.jpg"),
        Files.readAllLines(csv));
    assertFalse(Files.exists(dir.resolve("R42-20261012T020000Z.csv.part")));
  }

  @Test
  void imagesAreStoredByRetailerAndProduct() throws IOException {
    ImageStore store = new FileSystemImageStore(dir);
    ImageKey key = new ImageKey("R42", "TS-100", "IMG-1");

    assertFalse(store.contains(key));
    store.store(key, new byte[] {1, 2, 3});

    assertTrue(store.contains(key));
    assertArrayEquals(new byte[] {1, 2, 3}, Files.readAllBytes(dir.resolve("R42/TS-100/IMG-1.jpg")));
  }

  @Test
  void idsThatCouldEscapeTheFolderAreRejected() {
    assertThrows(InvalidRetailerDataException.class, () -> new ImageKey("R42", "..", "x"));
    assertThrows(InvalidRetailerDataException.class, () -> new ImageKey("R42", "a/b", "x"));
    assertThrows(InvalidRetailerDataException.class, () -> new ImageKey("R42", "TS-1", ""));
  }

  @Test
  void chapterOnesImageRulesLiveInTheAdapter() {
    String[] valid = {"TS-100", "tee", "Y", "Y", null, "IMG-7", "IMG-8"};
    String[] invalid = {"TS-100", "tee", "Y", "N", "IMG-1", null, null};

    assertEquals(Optional.of("IMG-7"), PartnerImageRow.validImageId(valid));
    assertEquals(Optional.empty(), PartnerImageRow.validImageId(invalid));
  }
}
