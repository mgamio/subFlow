package examples.ch09.suppliers;

/** A client error: the API answers 400 Bad Request with this message. */
public class InvalidQueryException extends IllegalArgumentException {
  public InvalidQueryException(String message) {
    super(message);
  }
}
