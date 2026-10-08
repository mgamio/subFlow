package com.subflow.domain;

import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.partnerCustomer;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class PricingRulesTest {
  private static final Campaign BLACK_FRIDAY = new Campaign(
      "Black Friday", new BigDecimal("0.30"),
      LocalDate.of(2026, 11, 27), LocalDate.of(2026, 11, 30));

  private Pricing pricingOn(LocalDate day) {
    return new Pricing(List.of(new PartnerRule(),
        new CampaignRule(BLACK_FRIDAY)), clockOn(day));
  }

  @Test
  void campaignPriceAppliesWhileTheCampaignRuns() {
    PricingPolicy policy = pricingOn(LocalDate.of(2026, 11, 28))
        .forCustomer(regularCustomer());
    assertEquals(Money.usd("167.92"),
        policy.priceFor(Plan.PRO, BillingCycle.ANNUAL));
    assertEquals(Money.usd("19.99"),
        policy.priceFor(Plan.PRO, BillingCycle.MONTHLY));
  }

  @Test
  void standardPriceAppliesOutsideTheCampaign() {
    assertEquals(Money.usd("203.90"), pricingOn(LocalDate.of(2026, 12, 1))
        .forCustomer(regularCustomer())
        .priceFor(Plan.PRO, BillingCycle.ANNUAL));
  }

  @Test
  void partnersKeepTheirPriceDuringACampaign() {
    assertEquals(Money.usd("179.91"), pricingOn(LocalDate.of(2026, 11, 28))
        .forCustomer(partnerCustomer())
        .priceFor(Plan.PRO, BillingCycle.ANNUAL));
  }
}
