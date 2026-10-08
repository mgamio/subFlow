package com.subflow;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Customer;
import com.subflow.domain.PartnerAgreement;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;

public final class TestData {
  public static final LocalDate START = LocalDate.of(2026, 1, 1);

  private TestData() { }

  public static Clock clockOn(LocalDate day) {
    return Clock.fixed(day.atStartOfDay(ZoneOffset.UTC).toInstant(),
        ZoneOffset.UTC);
  }

  public static Subscription annualPro() {
    return new Subscription(7, 1, Plan.PRO, BillingCycle.ANNUAL,
        SubscriptionStatus.ACTIVE, START, null);
  }

  public static Subscription withStatus(SubscriptionStatus status) {
    return new Subscription(8, 1, Plan.BASIC, BillingCycle.MONTHLY,
        status, START, null);
  }

  public static Customer regularCustomer() {
    return new Customer(1, "Ana Torres", "ana@example.com");
  }

  public static Customer partnerCustomer() {
    return new Customer(2, "Luis Rojas", "luis@acme.example",
        new PartnerAgreement("ACME", new BigDecimal("0.25")));
  }
}
