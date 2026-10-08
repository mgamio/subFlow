package examples.ch03.ocp.ratelimit.before;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** The plans can only come from a text file. */
public class RateLimiter {
  private final Map<String, Long> requestsPerMinute;

  public RateLimiter(Path plansFile) {
    this.requestsPerMinute = readPlans(plansFile);
  }

  public boolean allow(String plan, long requestsInLastMinute) {
    return requestsInLastMinute < requestsPerMinute.getOrDefault(plan, 0L);
  }

  private static Map<String, Long> readPlans(Path plansFile) {
    try {
      Map<String, Long> plans = new HashMap<>();
      List<String> lines = Files.readAllLines(plansFile);
      for (String line : lines) {
        String[] parts = line.split(":");
        plans.put(parts[0].strip(), Long.parseLong(parts[1].strip()));
      }
      return plans;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
