package examples.ch09.ordering;

/** The order breaks a business rule; the API answers 422. */
public class InvalidOrderException extends RuntimeException {
  public InvalidOrderException(String message) {
    super(message);
  }
}
