package examples.ch03.ocp.ratelimit.after;

import java.util.Map;

public class RateLimiter {
  private final Map<String, Long> requestsPerMinute;

  public RateLimiter(ApiPlanSource plans) {
    this.requestsPerMinute = Map.copyOf(plans.requestsPerMinute());
  }

  public boolean allow(String plan, long requestsInLastMinute) {
    return requestsInLastMinute < requestsPerMinute.getOrDefault(plan, 0L);
  }
}
