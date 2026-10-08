package com.subflow.testing;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.service.RevenueByPlan;
import com.subflow.service.RevenueQueries;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** The in-memory twin of JdbcRevenueQueries, for tests. */
public class InMemoryRevenue implements RevenueQueries {
  private final List<Payment> payments;
  private final Map<Long, Subscription> subscriptions;

  public InMemoryRevenue(List<Payment> payments, Map<Long, Subscription> subscriptions) {
    this.payments = payments;
    this.subscriptions = subscriptions;
  }

  @Override
  public List<RevenueByPlan> revenueByPlan(YearMonth month) {
    Map<Plan, RevenueByPlan> rows = new TreeMap<>();
    for (Payment p : payments) {
      if (YearMonth.from(p.date()).equals(month)) {
        Plan plan = subscriptions.get(p.subscriptionId()).plan();
        RevenueByPlan old = rows.get(plan);
        rows.put(plan, old == null
            ? new RevenueByPlan(plan, 1, p.amount())
            : new RevenueByPlan(plan, old.payments() + 1, old.revenue().plus(p.amount())));
      }
    }
    return new ArrayList<>(rows.values());
  }
}
