package examples.ch09.suppliers;

/** Which slice of a list the client wants. Every list endpoint uses it. */
public record PageRequest(int offset, int limit) {
  public static final int MAX_LIMIT = 100;

  public PageRequest {
    if (offset < 0) {
      throw new InvalidQueryException("offset must be 0 or more");
    }
    if (limit < 1 || limit > MAX_LIMIT) {
      throw new InvalidQueryException("limit must be between 1 and " + MAX_LIMIT);
    }
  }

  public static PageRequest first(int limit) {
    return new PageRequest(0, limit);
  }
}
