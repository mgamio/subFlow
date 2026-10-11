package examples.ch09.ordering.api;

/** An HTTP status and a body, independent of any web framework. */
public record ApiResponse(int status, Object body) {

  public static ApiResponse problem(int status, String title, String detail) {
    return new ApiResponse(status, new Problem(title, detail));
  }

  /** One error format for every endpoint (RFC 9457, simplified; see Chapter 6). */
  public record Problem(String title, String detail) {
  }
}
