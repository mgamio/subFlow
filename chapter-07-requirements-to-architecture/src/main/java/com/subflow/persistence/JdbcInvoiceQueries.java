package com.subflow.persistence;

import com.subflow.domain.Money;
import com.subflow.service.InvoiceQueries;
import com.subflow.service.InvoiceSummary;
import com.subflow.service.Page;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/** Keyset pagination: "after id X", not "skip N rows". */
public class JdbcInvoiceQueries implements InvoiceQueries {
  private static final String SQL =
      "SELECT i.id, i.subscription_id, i.issued_on, i.total, i.currency "
      + "FROM invoices i JOIN subscriptions s ON s.id = i.subscription_id "
      + "WHERE s.customer_id = ? AND i.id > ? "
      + "ORDER BY i.id LIMIT ?";

  private final DataSource dataSource;

  public JdbcInvoiceQueries(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Page<InvoiceSummary> invoicesOf(long customerId,
      Optional<String> cursor, int limit) {
    long afterId = cursor.map(InvoiceCursor::decode).orElse(0L);
    try (Connection db = dataSource.getConnection();
         PreparedStatement query = db.prepareStatement(SQL)) {
      query.setLong(1, customerId);
      query.setLong(2, afterId);
      query.setInt(3, limit + 1);                // one extra: is there more?
      List<InvoiceSummary> rows = new ArrayList<>();
      try (ResultSet rs = query.executeQuery()) {
        while (rs.next()) {
          rows.add(new InvoiceSummary(rs.getLong("id"),
              rs.getLong("subscription_id"),
              rs.getDate("issued_on").toLocalDate(),
              new Money(rs.getBigDecimal("total"),
                        Currency.getInstance(rs.getString("currency")))));
        }
      }
      return InvoiceCursor.page(rows, limit);
    } catch (SQLException e) {
      throw new IllegalStateException("Could not list invoices", e);
    }
  }
}
