package com.subflow.persistence;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.service.RenewalQueries;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/** A coarse filter in SQL; the exact rule stays in Subscription. */
public class JdbcRenewalQueries implements RenewalQueries {
  private static final String SQL = """
      SELECT id, customer_id, plan, start_date, end_date
        FROM subscriptions
       WHERE cycle = 'ANNUAL' AND status = 'ACTIVE'
         AND EXTRACT(MONTH FROM start_date) = ?
         AND EXTRACT(DAY FROM start_date) IN (?, ?)
      """;

  private final DataSource dataSource;

  public JdbcRenewalQueries(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Subscription> activeAnnualRenewingOn(LocalDate renewalDate) {
    int day = renewalDate.getDayOfMonth();
    boolean feb28 = renewalDate.getMonthValue() == 2 && day == 28;
    try (Connection db = dataSource.getConnection();
         PreparedStatement query = db.prepareStatement(SQL)) {
      query.setInt(1, renewalDate.getMonthValue());
      query.setInt(2, day);
      query.setInt(3, feb28 ? 29 : day);   // leap-day starts renew on Feb 28
      List<Subscription> rows = new ArrayList<>();
      try (ResultSet rs = query.executeQuery()) {
        while (rs.next()) {
          Date end = rs.getDate("end_date");
          rows.add(new Subscription(rs.getLong("id"), rs.getLong("customer_id"),
              Plan.valueOf(rs.getString("plan")), BillingCycle.ANNUAL,
              SubscriptionStatus.ACTIVE, rs.getDate("start_date").toLocalDate(),
              end == null ? null : end.toLocalDate()));
        }
      }
      return rows;
    } catch (SQLException e) {
      throw new IllegalStateException("Could not load renewal candidates", e);
    }
  }
}
