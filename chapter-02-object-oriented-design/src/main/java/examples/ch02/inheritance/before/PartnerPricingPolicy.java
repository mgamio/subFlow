package examples.ch02.inheritance.before;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import java.math.BigDecimal;

public class PartnerPricingPolicy extends PricingPolicy {
  private static final BigDecimal PARTNER_DISCOUNT =
      new BigDecimal("0.25");

  @Override
  public Money priceFor(Plan plan, BillingCycle cycle) {
    return plan.listPrice(cycle).times(
        BigDecimal.ONE.subtract(PARTNER_DISCOUNT));
  }
}
