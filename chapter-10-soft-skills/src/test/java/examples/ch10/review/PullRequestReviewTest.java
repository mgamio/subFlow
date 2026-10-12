package examples.ch10.review;

import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Campaign;
import com.subflow.domain.CampaignRule;
import com.subflow.domain.Customer;
import com.subflow.domain.Money;
import com.subflow.domain.PartnerRule;
import com.subflow.domain.Plan;
import examples.ch10.review.before.Pricing;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** The two defects the review found in the pull request, as tests. */
class PullRequestReviewTest {
  static final Campaign NEW_YEAR = new Campaign("New Year",
      new BigDecimal("0.30"), LocalDate.of(2027, 1, 1),
      LocalDate.of(2027, 1, 31));

  final Pricing pricing = new Pricing(
      List.of(new PartnerRule(), new CampaignRule(NEW_YEAR)),
      clockOn(LocalDate.of(2027, 1, 10)));

  @Test
  void aStudentAtAPeruvianUniversityGetsNoDiscount() {
    Customer maria = new Customer(3, "Maria Quispe", "maria@pucp.edu.pe");

    assertEquals(Money.usd("19.99"), pricing.forCustomer(maria)
        .priceFor(Plan.PRO, BillingCycle.MONTHLY));
  }

  @Test
  void duringACampaignAStudentPaysMoreThanEveryoneElse() {
    Customer sam = new Customer(4, "Sam Lee", "sam@mit.edu");

    assertEquals(Money.usd("191.90"), pricing.forCustomer(sam)
        .priceFor(Plan.PRO, BillingCycle.ANNUAL));           // student
    assertEquals(Money.usd("167.92"), pricing.forCustomer(regularCustomer())
        .priceFor(Plan.PRO, BillingCycle.ANNUAL));           // everyone else
  }
}
