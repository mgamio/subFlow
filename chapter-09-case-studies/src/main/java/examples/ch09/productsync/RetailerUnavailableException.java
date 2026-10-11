package examples.ch09.productsync;

/** The retailer's system is down. The run stops, and nothing is published. */
public class RetailerUnavailableException extends RuntimeException {
  public RetailerUnavailableException(String message) {
    super(message);
  }
}
