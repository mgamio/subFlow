package examples.ch01.cleancode.after;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class FileRestrictedProviders
    implements RestrictedProviders {
  private final Set<String> providerIds;

  public FileRestrictedProviders(Path file)
      throws IOException {
    this.providerIds = Files.readAllLines(file).stream()
        .map(String::strip)
        .filter(line -> !line.isEmpty())
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public boolean contains(String providerId) {
    return providerIds.contains(providerId);
  }
}
