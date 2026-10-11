package examples.ch09.productsync;

/** One product or image is missing. The run continues without it. */
public class NotFoundAtRetailerException extends RuntimeException {
  public NotFoundAtRetailerException(String message) {
    super(message);
  }
}
