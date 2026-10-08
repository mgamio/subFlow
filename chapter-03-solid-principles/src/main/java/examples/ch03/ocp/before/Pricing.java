package examples.ch03.ocp.before;

import com.subflow.domain.Campaign;
import com.subflow.domain.Customer;
import com.subflow.domain.PartnerPricing;
import com.subflow.domain.PricingPolicy;
import com.subflow.domain.PromotionalPricing;
import com.subflow.domain.StandardPricing;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

/** Every campaign means editing the class that decides every price. */
public class Pricing {
  private static final Campaign SPRING_SALE = new Campaign(
      "Spring Sale", new BigDecimal("0.25"),
      LocalDate.of(2026, 3, 20), LocalDate.of(2026, 4, 3));
  private static final Campaign BLACK_FRIDAY = new Campaign(
      "Black Friday", new BigDecimal("0.30"),
      LocalDate.of(2026, 11, 27), LocalDate.of(2026, 11, 30));

  private final PricingPolicy standard = new StandardPricing();
  private final Clock clock;

  public Pricing(Clock clock) {
    this.clock = clock;
  }

  public PricingPolicy forCustomer(Customer customer) {
    if (customer.partner().isPresent()) {
      return new PartnerPricing(customer.partner().get());
    }
    LocalDate today = LocalDate.now(clock);
    if (SPRING_SALE.isRunningOn(today)) {
      return new PromotionalPricing(SPRING_SALE);
    }
    if (BLACK_FRIDAY.isRunningOn(today)) {
      return new PromotionalPricing(BLACK_FRIDAY);
    }
    return standard;
  }
}
