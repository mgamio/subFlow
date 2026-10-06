package examples.ch01.yagni.before;

import com.subflow.domain.Subscription;
import java.math.BigDecimal;
import java.util.List;

public class DiscountEngine {
  private final List<DiscountRule> rules;

  public DiscountEngine(List<DiscountRule> rules) {
    this.rules = rules;
  }

  public BigDecimal apply(Subscription subscription,
                          BigDecimal price) {
    BigDecimal result = price;
    for (DiscountRule rule : rules) {
      if (rule.appliesTo(subscription)) {
        result = rule.apply(result);
      }
    }
    return result;
  }
}
