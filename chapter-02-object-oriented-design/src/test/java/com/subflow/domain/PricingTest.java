package com.subflow.domain;

import static com.subflow.TestData.partnerCustomer;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricingTest {
  private final Pricing pricing = new Pricing();

  @Test
  void regularCustomersGetTheAnnualDiscount() {
    assertEquals(Money.usd("203.90"), pricing.forCustomer(regularCustomer())
        .priceFor(Plan.PRO, BillingCycle.ANNUAL));
    assertEquals(Money.usd("19.99"), pricing.forCustomer(regularCustomer())
        .priceFor(Plan.PRO, BillingCycle.MONTHLY));
  }

  @Test
  void partnersGetTheirDiscountInsteadOfTheAnnualOne() {
    PricingPolicy partner = pricing.forCustomer(partnerCustomer());
    assertEquals(Money.usd("179.91"),
        partner.priceFor(Plan.PRO, BillingCycle.ANNUAL));
    assertEquals(Money.usd("14.99"),
        partner.priceFor(Plan.PRO, BillingCycle.MONTHLY));
  }

  @Test
  void aPartnerDiscountMustBeBetweenZeroAndOne() {
    assertThrows(IllegalArgumentException.class,
        () -> new PartnerAgreement("ACME", new BigDecimal("1.25")));
    assertThrows(IllegalArgumentException.class,
        () -> new PartnerAgreement("ACME", BigDecimal.ZERO));
  }
}
