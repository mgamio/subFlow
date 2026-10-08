package examples.ch02.polymorphism.before;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import com.subflow.domain.StandardPricing;
import java.math.BigDecimal;

/** The same if/else also lives in InvoiceService and RenewalService. */
public class CheckoutService {
  private final StandardPricing standardPricing = new StandardPricing();

  public Money priceFor(CustomerAccount customer, Plan plan,
                        BillingCycle cycle) {
    if (customer.isPartner()) {
      return plan.listPrice(cycle).times(BigDecimal.ONE
          .subtract(customer.getPartnerDiscount()));
    }
    return standardPricing.priceFor(plan, cycle);
  }
}
