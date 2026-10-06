package examples.ch01.yagni.before;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DiscountRuleFactory {

  /** Reads one class name per line and creates the rules by reflection. */
  public static List<DiscountRule> load(Path config) throws IOException {
    List<DiscountRule> rules = new ArrayList<>();
    for (String className : Files.readAllLines(config)) {
      if (className.isBlank()) {
        continue;
      }
      try {
        rules.add((DiscountRule) Class.forName(className.strip())
            .getDeclaredConstructor().newInstance());
      } catch (ReflectiveOperationException e) {
        throw new IllegalStateException("Cannot create " + className, e);
      }
    }
    return rules;
  }
}
