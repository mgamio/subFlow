package examples.ch09.productsync;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * CSV files in the folder the import process watches. Each file is written as
 * "name.csv.part" and renamed to "name.csv" only when it is complete.
 */
public class CsvImportFiles implements ImportFiles {
  private static final DateTimeFormatter STAMP =
      DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC);
  static final String HEADER = "retailer_id,product_id,name,price,currency,image_path";

  private final Path folder;

  public CsvImportFiles(Path folder) {
    this.folder = folder;
  }

  @Override
  public ImportFile create(String retailerId, Instant runStartedAt) {
    String name = retailerId + "-" + STAMP.format(runStartedAt) + ".csv";  // R42-20261012T020000Z.csv
    return new CsvImportFile(folder.resolve(name));
  }

  private static final class CsvImportFile implements ImportFile {
    private final Path target;
    private final Path partial;
    private final BufferedWriter out;

    CsvImportFile(Path target) {
      this.target = target;
      this.partial = target.resolveSibling(target.getFileName() + ".part");
      try {
        Files.createDirectories(target.getParent());
        this.out = Files.newBufferedWriter(partial, StandardCharsets.UTF_8);
        out.write(HEADER);
        out.newLine();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    @Override
    public void add(ImportLine line) {
      try {
        out.write(String.join(",", csv(line.retailerId()), csv(line.productId()),
            csv(line.name()), line.price().amount().toPlainString(),
            line.price().currency().getCurrencyCode(), csv(line.imagePath())));
        out.newLine();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    @Override
    public void publish() {
      try {
        out.close();
        Files.move(partial, target, StandardCopyOption.ATOMIC_MOVE);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    /** Quotes a value that contains a comma, a quote or a line break. */
    private static String csv(String value) {
      if (value.matches("[^,\"\r\n]*")) {
        return value;
      }
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
  }
}
