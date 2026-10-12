package com.subflow.domain;

import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.partnerCustomer;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class PricingTest {
  static final Campaign NEW_YEAR = new Campaign("New Year",
      new BigDecimal("0.30"), LocalDate.of(2027, 1, 1),
      LocalDate.of(2027, 1, 31));
  static final Customer MARIA =
      new Customer(3, "Maria Quispe", "maria@pucp.edu.pe");

  final List<PricingRule> rules = List.of(new PartnerRule(),
      new StudentRule(StudentRule.ACADEMIC_DOMAINS),
      new CampaignRule(NEW_YEAR));
  final Pricing inJanuary =
      new Pricing(rules, clockOn(LocalDate.of(2027, 1, 10)));
  final Pricing inFebruary =
      new Pricing(rules, clockOn(LocalDate.of(2027, 2, 10)));

  @Test
  void aStudentAtAPeruvianUniversityPaysTwentyPercentLess() {
    assertEquals(Money.usd("15.99"), monthlyPro(inFebruary, MARIA));
    assertEquals(Money.usd("191.90"), annualPro(inFebruary, MARIA));
  }

  @Test
  void aDomainThatOnlyLooksAcademicGetsTheStandardPrice() {
    Customer joe = new Customer(5, "Joe Ruiz", "joe@notedu.pe");

    assertEquals(Money.usd("19.99"), monthlyPro(inFebruary, joe));
  }

  @Test
  void duringACampaignAStudentPaysTheLowerOfBothPrices() {
    assertEquals(Money.usd("167.92"), annualPro(inJanuary, MARIA));
    assertEquals(Money.usd("15.99"), monthlyPro(inJanuary, MARIA));
  }

  @Test
  void offersNeverCombine() {
    Customer partner = partnerCustomer();               // 25% off
    assertEquals(Money.usd("179.91"), annualPro(inFebruary, partner));
    assertEquals(Money.usd("167.92"), annualPro(inJanuary, partner));
    assertEquals(Money.usd("203.90"),
        annualPro(inFebruary, regularCustomer()));      // standard: 15%
  }

  private static Money monthlyPro(Pricing pricing, Customer customer) {
    return pricing.forCustomer(customer)
        .priceFor(Plan.PRO, BillingCycle.MONTHLY);
  }

  private static Money annualPro(Pricing pricing, Customer customer) {
    return pricing.forCustomer(customer)
        .priceFor(Plan.PRO, BillingCycle.ANNUAL);
  }
}
