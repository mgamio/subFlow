package examples.ch01.yagni.before;

import com.subflow.domain.Subscription;
import java.math.BigDecimal;

public interface DiscountRule {
  boolean appliesTo(Subscription subscription);
  BigDecimal apply(BigDecimal price);
}
