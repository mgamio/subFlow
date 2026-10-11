package examples.ch09.productsync;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * The weekly job for one retailer. It can be run again at any time: images
 * already stored are skipped, so a failed run is recovered by running it again.
 */
public class ProductSync {
  private final String retailerId;
  private final MarketplaceProducts marketplace;
  private final RetailerCatalog retailer;
  private final ImageStore images;
  private final ImportFiles importFiles;
  private final Clock clock;

  public ProductSync(String retailerId, MarketplaceProducts marketplace,
      RetailerCatalog retailer, ImageStore images, ImportFiles importFiles, Clock clock) {
    this.retailerId = retailerId;
    this.marketplace = marketplace;
    this.retailer = retailer;
    this.images = images;
    this.importFiles = importFiles;
    this.clock = clock;
  }

  /** @throws RetailerUnavailableException if the retailer is down; nothing is published */
  public SyncReport run() {
    Instant startedAt = clock.instant();
    LocalDate today = LocalDate.ofInstant(startedAt, clock.getZone());
    ImportFile file = importFiles.create(retailerId, startedAt);
    SyncReport report = new SyncReport();
    for (String productId : marketplace.listedProductIds(retailerId, today)) {
      try {
        syncProduct(retailer.product(productId), file, report);
      } catch (NotFoundAtRetailerException | InvalidRetailerDataException e) {
        report.failed(productId + ": " + e.getMessage());
      }
    }
    file.publish();
    return report;
  }

  private void syncProduct(RetailerProduct product, ImportFile file, SyncReport report) {
    List<ImageKey> keys = product.imageIds().stream()   // check all ids first
        .map(imageId -> new ImageKey(retailerId, product.productId(), imageId))
        .toList();
    for (ImageKey key : keys) {
      if (images.contains(key)) {
        report.imageAlreadyStored();
      } else {
        images.store(key, retailer.image(key.imageId()));
        report.imageStored();
      }
      file.add(new ImportLine(retailerId, product.productId(), product.name(),
          product.price(), key.path()));
    }
    report.productSynced();
  }
}
