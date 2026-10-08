package examples.ch03.ocp.ratelimit.after;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

public class JdbcApiPlanSource implements ApiPlanSource {
  private final DataSource dataSource;

  public JdbcApiPlanSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Map<String, Long> requestsPerMinute() {
    String sql = "SELECT name, requests_per_minute FROM api_plans";
    try (Connection db = dataSource.getConnection();
         PreparedStatement query = db.prepareStatement(sql);
         ResultSet rs = query.executeQuery()) {
      Map<String, Long> plans = new HashMap<>();
      while (rs.next()) {
        plans.put(rs.getString(1), rs.getLong(2));
      }
      return plans;
    } catch (SQLException e) {
      throw new IllegalStateException("Could not load API plans", e);
    }
  }
}
