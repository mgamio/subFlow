package examples.ch02.inheritance.before;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import com.subflow.domain.RenewalQuote;
import com.subflow.domain.Subscription;
import java.math.BigDecimal;

public class PricingPolicy {
  protected static final BigDecimal ANNUAL_DISCOUNT =
      new BigDecimal("0.15");

  public Money priceFor(Plan plan, BillingCycle cycle) {
    Money listPrice = plan.listPrice(cycle);
    if (cycle == BillingCycle.MONTHLY) {
      return listPrice;
    }
    return listPrice.times(
        BigDecimal.ONE.subtract(ANNUAL_DISCOUNT));
  }

  // Added six months later, for renewal reminder emails
  public RenewalQuote renewalQuote(Subscription subscription) {
    Money listPrice = subscription.plan()
        .listPrice(subscription.cycle());
    Money price = subscription.cycle() == BillingCycle.ANNUAL
        ? listPrice.times(BigDecimal.ONE.subtract(ANNUAL_DISCOUNT))
        : listPrice;
    return new RenewalQuote(listPrice, price);
  }
}
