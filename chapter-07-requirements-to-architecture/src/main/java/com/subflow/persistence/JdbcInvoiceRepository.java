package com.subflow.persistence;

import com.subflow.domain.Invoice;
import com.subflow.domain.Money;
import com.subflow.service.InvoiceRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import javax.sql.DataSource;

public class JdbcInvoiceRepository implements InvoiceRepository {
  private final DataSource dataSource;
  private final Clock clock;

  public JdbcInvoiceRepository(DataSource dataSource, Clock clock) {
    this.dataSource = dataSource;
    this.clock = clock;
  }

  @Override
  public void save(Invoice invoice) {
    Money total = invoice.total();
    // The columns JdbcInvoiceQueries reads (see V0__baseline.sql).
    String sql = "INSERT INTO invoices (subscription_id, customer_email, "
        + "total, currency, issued_on) VALUES (?, ?, ?, ?, ?)";
    try (Connection db = dataSource.getConnection();
         PreparedStatement insert = db.prepareStatement(sql)) {
      insert.setLong(1, invoice.subscriptionId());
      insert.setString(2, invoice.customerEmail());
      insert.setBigDecimal(3, total.amount());
      insert.setString(4, total.currency().getCurrencyCode());
      insert.setDate(5, Date.valueOf(LocalDate.now(clock)));
      insert.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException("Could not save invoice", e);
    }
  }
}
