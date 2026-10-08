package com.subflow.persistence;

import com.subflow.domain.Invoice;
import com.subflow.domain.Money;
import com.subflow.service.InvoiceRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcInvoiceRepository implements InvoiceRepository {
  private final DataSource dataSource;

  public JdbcInvoiceRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void save(Invoice invoice) {
    Money total = invoice.total();
    String sql = "INSERT INTO invoices (subscription_id, "
        + "customer_email, amount, currency) VALUES (?, ?, ?, ?)";
    try (Connection db = dataSource.getConnection();
         PreparedStatement insert = db.prepareStatement(sql)) {
      insert.setLong(1, invoice.subscriptionId());
      insert.setString(2, invoice.customerEmail());
      insert.setBigDecimal(3, total.amount());
      insert.setString(4, total.currency().getCurrencyCode());
      insert.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException("Could not save invoice", e);
    }
  }
}
