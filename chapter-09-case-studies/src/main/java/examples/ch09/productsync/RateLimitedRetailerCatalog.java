package examples.ch09.productsync;

import java.util.function.Supplier;

/** A decorator: waits as long as the retailer asks, then tries again. */
public class RateLimitedRetailerCatalog implements RetailerCatalog {
  private final RetailerCatalog retailer;
  private final int maxAttempts;
  private final Sleeper sleeper;

  public RateLimitedRetailerCatalog(RetailerCatalog retailer, int maxAttempts,
      Sleeper sleeper) {
    this.retailer = retailer;
    this.maxAttempts = maxAttempts;
    this.sleeper = sleeper;
  }

  @Override
  public RetailerProduct product(String productId) {
    return withRetries(() -> retailer.product(productId));
  }

  @Override
  public byte[] image(String imageId) {
    return withRetries(() -> retailer.image(imageId));
  }

  private <T> T withRetries(Supplier<T> call) {
    for (int attempt = 1; ; attempt++) {
      try {
        return call.get();
      } catch (RetailerRateLimitedException e) {
        if (attempt == maxAttempts) {
          throw e;
        }
        sleeper.sleep(e.retryAfter());
      }
    }
  }
}
