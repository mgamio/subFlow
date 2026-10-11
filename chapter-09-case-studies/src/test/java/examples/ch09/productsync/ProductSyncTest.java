package examples.ch09.productsync;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductSyncTest {
  final Clock monday = Clock.fixed(Instant.parse("2026-10-12T02:00:00Z"), ZoneOffset.UTC);
  final FakeRetailer retailer = new FakeRetailer()
      .product("TS-100", "Basic tee, white", "9.90", "IMG-1", "IMG-2")
      .product("TS-200", "Logo tee, navy", "14.90", "IMG-3");
  final MarketplaceProducts listed = (retailerId, day) -> {
    assertEquals(LocalDate.of(2026, 10, 12), day);
    return List.of("TS-100", "TS-200");
  };
  final InMemoryImageStore images = new InMemoryImageStore();
  final InMemoryImportFiles importFiles = new InMemoryImportFiles();

  ProductSync sync() {
    return new ProductSync("R42", listed, retailer, images, importFiles, monday);
  }

  @Test
  void storesEveryImageAndPublishesOneLinePerImage() {
    SyncReport report = sync().run();

    assertEquals(2, report.products());
    assertEquals(3, report.imagesStored());
    assertTrue(images.contains(new ImageKey("R42", "TS-100", "IMG-2")));
    List<ImportLine> lines = importFiles.published.get(0);
    assertEquals(3, lines.size());
    assertEquals("R42/TS-200/IMG-3.jpg", lines.get(2).imagePath());
  }

  @Test
  void aSecondRunDownloadsNothingAgain() {
    sync().run();
    retailer.imageDownloads.clear();

    SyncReport again = sync().run();

    assertEquals(List.of(), retailer.imageDownloads);
    assertEquals(3, again.imagesAlreadyStored());
    assertEquals(3, importFiles.published.get(1).size());   // prices may have changed
  }

  @Test
  void aMissingImageIsReportedAndTheRunContinues() {
    retailer.missingImage("IMG-2");

    SyncReport report = sync().run();

    assertEquals(List.of("TS-100: image IMG-2 not found"), report.failures());
    assertTrue(images.contains(new ImageKey("R42", "TS-200", "IMG-3")));
  }

  @Test
  void runningAgainRepairsAFailedRun() {
    retailer.missingImage("IMG-2");
    sync().run();
    retailer.imageIsBack("IMG-2");
    retailer.imageDownloads.clear();

    SyncReport repair = sync().run();

    assertEquals(List.of("IMG-2"), retailer.imageDownloads);
    assertEquals(List.of(), repair.failures());
  }

  @Test
  void ifTheRetailerGoesDownNothingIsPublished() {
    retailer.downAfterProduct = "TS-200";

    assertThrows(RetailerUnavailableException.class, () -> sync().run());

    assertEquals(List.of(), importFiles.published);
  }

  @Test
  void anUnsafeIdFromTheRetailerIsRefused() {
    retailer.product("TS-300", "Hacker tee", "1.00", "../../etc/passwd");
    MarketplaceProducts onlyTheBadOne = (retailerId, day) -> List.of("TS-300");

    SyncReport report = new ProductSync("R42", onlyTheBadOne, retailer, images,
        importFiles, monday).run();

    assertEquals(1, report.failures().size());
    assertTrue(images.images.isEmpty());
    assertEquals(List.of(), importFiles.published.get(0));
  }
}
