package examples.ch09.productsync;

import java.time.Duration;

/** HTTP 429 Too Many Requests, with the wait the retailer asked for. */
public class RetailerRateLimitedException extends RetailerUnavailableException {
  private final Duration retryAfter;

  public RetailerRateLimitedException(Duration retryAfter) {
    super("Rate limited; retry after " + retryAfter);
    this.retryAfter = retryAfter;
  }

  public Duration retryAfter() {
    return retryAfter;
  }
}
