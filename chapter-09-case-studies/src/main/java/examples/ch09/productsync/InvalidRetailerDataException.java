package examples.ch09.productsync;

/** The retailer sent data we refuse to use, such as an unsafe id. */
public class InvalidRetailerDataException extends RuntimeException {
  public InvalidRetailerDataException(String message) {
    super(message);
  }
}
