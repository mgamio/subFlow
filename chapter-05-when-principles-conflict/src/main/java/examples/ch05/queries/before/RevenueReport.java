package examples.ch05.queries.before;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.service.RevenueByPlan;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/** Loads every payment and every subscription to add up a few numbers. */
public class RevenueReport {
  private final List<Payment> allPayments;
  private final Function<Long, Subscription> subscriptionById;

  public RevenueReport(List<Payment> allPayments,
                       Function<Long, Subscription> subscriptionById) {
    this.allPayments = allPayments;
    this.subscriptionById = subscriptionById;
  }

  public List<RevenueByPlan> revenueByPlan(YearMonth month) {
    Map<Plan, Long> counts = new EnumMap<>(Plan.class);
    Map<Plan, Money> totals = new EnumMap<>(Plan.class);
    for (Payment payment : allPayments) {
      if (!YearMonth.from(payment.date()).equals(month)) {
        continue;
      }
      Plan plan = subscriptionById.apply(payment.subscriptionId()).plan();
      counts.merge(plan, 1L, Long::sum);
      totals.merge(plan, payment.amount(), Money::plus);
    }
    List<RevenueByPlan> rows = new ArrayList<>();
    for (Plan plan : totals.keySet()) {
      rows.add(new RevenueByPlan(plan, counts.get(plan), totals.get(plan)));
    }
    return rows;
  }
}
