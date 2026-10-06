package examples.ch01.soc.before;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.sql.DataSource;

/** Problematic on purpose: five concerns in one method, and SQL injection. */
public class SubscriptionController {
  private final DataSource dataSource;

  public SubscriptionController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public String cancel(String requestBody) throws Exception {
    // 1. Read the request
    long id = Long.parseLong(
        requestBody.replaceAll("\\D", ""));

    try (Connection db = dataSource.getConnection()) {
      // 2. Load the data
      ResultSet rs = db.createStatement().executeQuery(
          "SELECT start_date FROM subscriptions WHERE id = "
          + id);
      rs.next();
      LocalDate start = rs.getDate(1).toLocalDate();

      // 3. Apply the business rule
      boolean refund = ChronoUnit.DAYS.between(
          start, LocalDate.now()) <= 14;

      // 4. Save the change
      db.createStatement().executeUpdate(
          "UPDATE subscriptions SET status = 'CANCELED'"
          + " WHERE id = " + id);

      // 5. Build the response
      return "{\"canceled\":true,\"refund\":" + refund + "}";
    }
  }
}
