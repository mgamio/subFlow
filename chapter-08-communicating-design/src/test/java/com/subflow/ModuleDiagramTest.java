package com.subflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * The module diagram is a text file next to the code. This test reads
 * its arrows and compares them with the imports, so the picture cannot
 * drift away from the code without a failing build.
 */
class ModuleDiagramTest {
  private static final Path DIAGRAM = Path.of("docs/diagrams/modules.mmd");
  private static final Path SOURCES = Path.of("src/main/java/com/subflow");
  private static final Pattern ARROW =
      Pattern.compile("^\\s*(\\w+)\\s*-->\\s*(\\w+)\\s*$", Pattern.MULTILINE);
  private static final Pattern IMPORT =
      Pattern.compile("^import\\s+(?:static\\s+)?com\\.subflow\\.(\\w+)", Pattern.MULTILINE);

  @Test
  void theDiagramShowsExactlyTheDependenciesInTheCode() throws IOException {
    assertEquals(arrowsInCode(), arrowsInDiagram());
  }

  private static Set<String> arrowsInDiagram() throws IOException {
    Set<String> arrows = new TreeSet<>();
    Matcher m = ARROW.matcher(Files.readString(DIAGRAM));
    while (m.find()) {
      arrows.add(m.group(1) + " --> " + m.group(2));
    }
    return arrows;
  }

  private static Set<String> arrowsInCode() throws IOException {
    Set<String> arrows = new TreeSet<>();
    try (Stream<Path> files = Files.walk(SOURCES)) {
      for (Path file : files.filter(f -> f.toString().endsWith(".java")).toList()) {
        String from = SOURCES.relativize(file).getName(0).toString();
        Matcher m = IMPORT.matcher(Files.readString(file));
        while (m.find()) {
          String to = m.group(1);
          if (isDrawn(from, to)) {
            arrows.add(from + " --> " + to);
          }
        }
      }
    }
    return arrows;
  }

  /** The legend says it: arrows to domain are drawn only from service. */
  private static boolean isDrawn(String from, String to) {
    if (to.equals(from)) {
      return false;
    }
    return !to.equals("domain") || from.equals("service");
  }
}
