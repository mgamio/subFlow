package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.TestData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Tries every operation on every status and compares the transitions
 * that succeed with the ones drawn in the state diagram.
 */
class SubscriptionStateDiagramTest {
  private static final Path DIAGRAM = Path.of("docs/diagrams/subscription-states.mmd");
  private static final Pattern TRANSITION = Pattern.compile(
      "^\\s*([A-Z_]+)\\s*-->\\s*([A-Z_]+)\\s*:\\s*(\\w+)\\(\\)", Pattern.MULTILINE);
  private static final Map<String, UnaryOperator<Subscription>> OPERATIONS = Map.of(
      "activate", Subscription::activate,
      "cancel", Subscription::cancel);

  @Test
  void theDiagramShowsExactlyTheTransitionsTheCodeAllows() throws IOException {
    assertEquals(transitionsInCode(), transitionsInDiagram());
  }

  private static Set<String> transitionsInDiagram() throws IOException {
    Set<String> transitions = new TreeSet<>();
    Matcher m = TRANSITION.matcher(Files.readString(DIAGRAM));
    while (m.find()) {
      transitions.add(m.group(1) + " --> " + m.group(2) + " : " + m.group(3) + "()");
    }
    return transitions;
  }

  private static Set<String> transitionsInCode() {
    Set<String> transitions = new TreeSet<>();
    for (SubscriptionStatus from : SubscriptionStatus.values()) {
      OPERATIONS.forEach((name, operation) -> {
        try {
          Subscription after = operation.apply(TestData.withStatus(from));
          transitions.add(from + " --> " + after.status() + " : " + name + "()");
        } catch (IllegalStateException notAllowed) {
          // the code refuses: the diagram must not show this arrow
        }
      });
    }
    return transitions;
  }
}
