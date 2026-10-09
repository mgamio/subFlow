package examples.ch06.errors.before;

import java.io.PrintWriter;
import java.io.StringWriter;

/** Whatever goes wrong, the caller gets a 500 and the stack trace. */
public class SubscriptionController {

  public record Response(int status, String body) { }

  public Response cancel(long subscriptionId, Runnable cancellation) {
    try {
      cancellation.run();
      return new Response(200, "OK");
    } catch (Exception e) {
      StringWriter trace = new StringWriter();
      e.printStackTrace(new PrintWriter(trace));
      return new Response(500, trace.toString());
    }
  }
}
