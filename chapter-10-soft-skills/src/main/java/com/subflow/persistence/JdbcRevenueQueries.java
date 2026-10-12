package com.subflow.persistence;

import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import com.subflow.service.RevenueByPlan;
import com.subflow.service.RevenueQueries;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import javax.sql.DataSource;

/** The database does the counting; no domain objects are loaded. */
public class JdbcRevenueQueries implements RevenueQueries {
  private static final String SQL = """
      SELECT s.plan, COUNT(*) AS payments, SUM(p.amount) AS revenue
        FROM payments p
        JOIN subscriptions s ON s.id = p.subscription_id
       WHERE p.payment_date >= ? AND p.payment_date < ?
         AND p.currency = 'USD'
       GROUP BY s.plan
       ORDER BY s.plan
      """;

  private final DataSource dataSource;

  public JdbcRevenueQueries(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<RevenueByPlan> revenueByPlan(YearMonth month) {
    try (Connection db = dataSource.getConnection();
         PreparedStatement query = db.prepareStatement(SQL)) {
      query.setDate(1, Date.valueOf(month.atDay(1)));
      query.setDate(2, Date.valueOf(month.plusMonths(1).atDay(1)));
      List<RevenueByPlan> rows = new ArrayList<>();
      try (ResultSet rs = query.executeQuery()) {
        while (rs.next()) {
          rows.add(new RevenueByPlan(
              Plan.valueOf(rs.getString("plan")),
              rs.getLong("payments"),
              new Money(rs.getBigDecimal("revenue"),
                        Currency.getInstance("USD"))));
        }
      }
      return rows;
    } catch (SQLException e) {
      throw new IllegalStateException("Could not read revenue", e);
    }
  }
}
