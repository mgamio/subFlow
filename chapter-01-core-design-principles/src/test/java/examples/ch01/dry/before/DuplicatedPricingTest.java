package examples.ch01.dry.before;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/** Shows how the two copies of the pricing rule have already drifted apart. */
class DuplicatedPricingTest {

  @Test
  void bothCopiesAgreeOnKnownPlans() {
    assertEquals(new BigDecimal("191.90"),
        new CheckoutService().priceFor("PRO", true));
    assertEquals(new BigDecimal("191.90"),
        new InvoiceService().amountDue(new Subscription("PRO", true)));
  }

  @Test
  void butTheyDisagreeOnUnknownPlans() {
    assertThrows(IllegalArgumentException.class,
        () -> new CheckoutService().priceFor("GOLD", false));
    assertEquals(new BigDecimal("9.99"),   // silently billed as Basic
        new InvoiceService().amountDue(new Subscription("GOLD", false)));
  }
}
