package examples.ch03.ocp.ratelimit.after;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileApiPlanSource implements ApiPlanSource {
  private final Path plansFile;

  public FileApiPlanSource(Path plansFile) {
    this.plansFile = plansFile;
  }

  @Override
  public Map<String, Long> requestsPerMinute() {
    try {
      Map<String, Long> plans = new HashMap<>();
      for (String line : Files.readAllLines(plansFile)) {
        String[] parts = line.split(":");
        plans.put(parts[0].strip(), Long.parseLong(parts[1].strip()));
      }
      return plans;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
