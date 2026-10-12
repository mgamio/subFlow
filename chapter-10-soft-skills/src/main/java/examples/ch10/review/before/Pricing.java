package examples.ch10.review.before;

import com.subflow.domain.Customer;
import com.subflow.domain.Money;
import com.subflow.domain.PricingPolicy;
import com.subflow.domain.PricingRule;
import com.subflow.domain.StandardPricing;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** SubFlow's Pricing as changed by the pull request under review. */
public class Pricing {
  private final List<PricingRule> rules;
  private final Clock clock;
  private final PricingPolicy standard = new StandardPricing();

  public Pricing(List<PricingRule> rules, Clock clock) {
    this.rules = List.copyOf(rules);
    this.clock = clock;
  }

  public PricingPolicy forCustomer(Customer customer) {
    // students get 20% off
    if (customer.email().endsWith(".edu")) {
      return (plan, cycle) -> {
        double price = plan.listPrice(cycle).amount().doubleValue() * 0.8;
        return Money.usd(String.valueOf(price));
      };
    }
    LocalDate today = LocalDate.now(clock);
    for (PricingRule rule : rules) {
      Optional<PricingPolicy> policy = rule.policyFor(customer, today);
      if (policy.isPresent()) {
        return policy.get();
      }
    }
    return standard;
  }
}
