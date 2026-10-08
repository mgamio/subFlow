package examples.ch03.ocp.ratelimit.after;

import java.util.Map;

public interface ApiPlanSource {
  /** Requests allowed per minute, by plan name. */
  Map<String, Long> requestsPerMinute();
}
