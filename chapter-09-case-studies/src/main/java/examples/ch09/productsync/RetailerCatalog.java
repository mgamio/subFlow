package examples.ch09.productsync;

/**
 * What the job needs from a retailer, named after the role, not the technology.
 * One adapter per retailer: a REST API today, perhaps SFTP files for the next one.
 */
public interface RetailerCatalog {
  /** @throws NotFoundAtRetailerException if the retailer no longer has it */
  RetailerProduct product(String productId);

  /** @throws NotFoundAtRetailerException if the retailer no longer has it */
  byte[] image(String imageId);
}
