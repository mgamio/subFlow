package com.subflow.persistence;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.service.ConcurrentUpdateException;
import com.subflow.service.SubscriptionRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

public class JdbcSubscriptionRepository implements SubscriptionRepository {
  private final DataSource dataSource;

  public JdbcSubscriptionRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Optional<Subscription> findById(long id) {
    String sql = "SELECT id, customer_id, plan, cycle, status, "
        + "start_date, end_date FROM subscriptions WHERE id = ?";
    try (Connection db = dataSource.getConnection();
         PreparedStatement query = db.prepareStatement(sql)) {
      query.setLong(1, id);
      try (ResultSet rs = query.executeQuery()) {
        if (!rs.next()) {
          return Optional.empty();
        }
        Date end = rs.getDate("end_date");
        return Optional.of(new Subscription(
            rs.getLong("id"),
            rs.getLong("customer_id"),
            Plan.valueOf(rs.getString("plan")),
            BillingCycle.valueOf(rs.getString("cycle")),
            SubscriptionStatus.valueOf(rs.getString("status")),
            rs.getDate("start_date").toLocalDate(),
            end == null ? null : end.toLocalDate()));
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Could not load subscription " + id, e);
    }
  }

  @Override
  public void save(Subscription s) {
    String sql = "INSERT INTO subscriptions (id, customer_id, plan, cycle, "
        + "status, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection db = dataSource.getConnection();
         PreparedStatement insert = db.prepareStatement(sql)) {
      insert.setLong(1, s.id());
      insert.setLong(2, s.customerId());
      insert.setString(3, s.plan().name());
      insert.setString(4, s.cycle().name());
      insert.setString(5, s.status().name());
      insert.setDate(6, Date.valueOf(s.startDate()));
      insert.setDate(7, s.endDate() == null ? null : Date.valueOf(s.endDate()));
      insert.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException("Could not save subscription " + s.id(), e);
    }
  }

  /** Optimistic concurrency: the WHERE clause checks nothing changed. */
  @Override
  public void update(Subscription expected, Subscription updated) {
    String sql = "UPDATE subscriptions SET status = ? "
        + "WHERE id = ? AND status = ?";
    try (Connection db = dataSource.getConnection();
         PreparedStatement update = db.prepareStatement(sql)) {
      update.setString(1, updated.status().name());
      update.setLong(2, expected.id());
      update.setString(3, expected.status().name());
      if (update.executeUpdate() == 0) {
        throw new ConcurrentUpdateException(
            "Subscription " + expected.id() + " was changed by someone else");
      }
    } catch (SQLException e) {
      throw new IllegalStateException(
          "Could not update subscription " + expected.id(), e);
    }
  }
}
