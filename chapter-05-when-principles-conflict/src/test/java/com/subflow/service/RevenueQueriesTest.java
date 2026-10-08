package com.subflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.testing.InMemoryRevenue;
import examples.ch05.queries.before.RevenueReport;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RevenueQueriesTest {
  private final Map<Long, Subscription> subscriptions = Map.of(
      1L, sub(1, Plan.BASIC), 2L, sub(2, Plan.PRO), 3L, sub(3, Plan.PRO));
  private final List<Payment> payments = List.of(
      pay("a", 1, "9.99", LocalDate.of(2026, 3, 2)),
      pay("b", 2, "19.99", LocalDate.of(2026, 3, 5)),
      pay("c", 3, "203.90", LocalDate.of(2026, 3, 31)),
      pay("d", 2, "19.99", LocalDate.of(2026, 4, 1)));

  @Test
  void revenueIsGroupedByPlanForOneMonth() {
    assertEquals(List.of(
        new RevenueByPlan(Plan.BASIC, 1, Money.usd("9.99")),
        new RevenueByPlan(Plan.PRO, 2, Money.usd("223.89"))),
        new InMemoryRevenue(payments, subscriptions).revenueByPlan(YearMonth.of(2026, 3)));
  }

  @Test
  void theOldReportGivesTheSameAnswer() {
    YearMonth march = YearMonth.of(2026, 3);
    assertEquals(new InMemoryRevenue(payments, subscriptions).revenueByPlan(march),
        new RevenueReport(payments, subscriptions::get).revenueByPlan(march));
  }

  private static Subscription sub(long id, Plan plan) {
    return new Subscription(id, id, plan, BillingCycle.MONTHLY,
        SubscriptionStatus.ACTIVE, LocalDate.of(2026, 1, 1), null);
  }

  private static Payment pay(String id, long subscriptionId, String amount, LocalDate date) {
    return new Payment(id, subscriptionId, PaymentProvider.PAYFAST, Money.usd(amount), date);
  }
}
