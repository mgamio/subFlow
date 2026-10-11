package com.subflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * The module rules of ADR 0004, checked on every build: a fitness
 * function. Each package may import only the packages listed for it.
 */
class ArchitectureRulesTest {
  private static final Path SOURCES = Path.of("src/main/java/com/subflow");
  private static final Pattern IMPORT =
      Pattern.compile("^import\\s+(?:static\\s+)?com\\.subflow\\.(\\w+)", Pattern.MULTILINE);

  private static final Map<String, Set<String>> ALLOWED = Map.of(
      "domain", Set.of("domain"),
      "service", Set.of("domain", "service"),
      "payment", Set.of("domain", "service", "payment"),
      "persistence", Set.of("domain", "service", "persistence"),
      "notification", Set.of("domain", "service", "notification"),
      "api", Set.of("domain", "service", "api"),
      "web", Set.of("domain", "service", "web"),
      "app", Set.of("domain", "service", "payment", "persistence",
                    "notification", "api", "web", "app"));

  @Test
  void everyPackageDependsOnlyOnWhatItIsAllowedTo() throws IOException {
    List<String> violations = new ArrayList<>();
    try (Stream<Path> files = Files.walk(SOURCES)) {
      for (Path file : files.filter(f -> f.toString().endsWith(".java")).toList()) {
        String module = SOURCES.relativize(file).getName(0).toString();
        Matcher m = IMPORT.matcher(Files.readString(file));
        while (m.find()) {
          if (!ALLOWED.get(module).contains(m.group(1))) {
            violations.add(SOURCES.relativize(file) + " imports com.subflow." + m.group(1));
          }
        }
      }
    }
    assertEquals(List.of(), violations);
  }

  @Test
  void everyPackageHasARule() throws IOException {
    try (Stream<Path> dirs = Files.list(SOURCES)) {
      dirs.filter(Files::isDirectory)
          .forEach(d -> assertEquals(true, ALLOWED.containsKey(d.getFileName().toString()),
              "no rule for package " + d.getFileName()));
    }
  }
}
