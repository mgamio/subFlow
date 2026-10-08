package examples.ch02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import com.subflow.domain.RenewalQuote;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import examples.ch02.encapsulation.before.SubscriptionRecord;
import examples.ch02.encapsulation.before.SupportTool;
import examples.ch02.inheritance.before.PartnerPricingPolicy;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** The problems described in the book, demonstrated. */
class BeforeExamplesTest {

  @Test
  void setterLetsSupportReactivateACanceledSubscription() {
    SubscriptionRecord subscription = new SubscriptionRecord();
    subscription.setStatus("CANCELED");
    subscription.setEndDate(LocalDate.of(2026, 3, 31));

    new SupportTool().fixAccount(subscription);

    assertEquals("ACTIVE", subscription.getStatus());   // no rule stopped it
  }

  @Test
  void bankAccountAcceptsANegativeDeposit() {
    var account = new examples.ch02.encapsulation.before.BankAccount(1000);
    account.deposit(-500);
    assertEquals(500.0, account.getBalance());
  }

  @Test
  void protectedBankAccountRejectsIt() {
    var account = new examples.ch02.encapsulation.after.BankAccount(
        new BigDecimal("1000"));
    assertThrows(IllegalArgumentException.class,
        () -> account.deposit(new BigDecimal("-500")));
  }

  @Test
  void inheritedRenewalQuoteIgnoresThePartnerOverride() {
    PartnerPricingPolicy partner = new PartnerPricingPolicy();
    Subscription annualPro = new Subscription(7, 2, Plan.PRO,
        BillingCycle.ANNUAL, SubscriptionStatus.ACTIVE,
        LocalDate.of(2026, 1, 1), null);

    Money charged = partner.priceFor(Plan.PRO, BillingCycle.ANNUAL);
    RenewalQuote quoted = partner.renewalQuote(annualPro);

    assertEquals(Money.usd("179.91"), charged);
    assertEquals(Money.usd("203.90"), quoted.price());
    assertNotEquals(charged, quoted.price());
  }
}
