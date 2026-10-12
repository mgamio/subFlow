package com.subflow.persistence;

import com.subflow.domain.Campaign;
import com.subflow.service.CampaignCatalog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcCampaignCatalog implements CampaignCatalog {
  private static final String SQL =
      "SELECT name, annual_discount, first_day, last_day FROM campaigns "
      + "WHERE last_day >= CURRENT_DATE ORDER BY first_day";

  private final DataSource dataSource;

  public JdbcCampaignCatalog(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Campaign> campaigns() {
    try (Connection db = dataSource.getConnection();
         PreparedStatement query = db.prepareStatement(SQL);
         ResultSet rs = query.executeQuery()) {
      List<Campaign> campaigns = new ArrayList<>();
      while (rs.next()) {
        campaigns.add(new Campaign(rs.getString("name"),
            rs.getBigDecimal("annual_discount"),
            rs.getDate("first_day").toLocalDate(),
            rs.getDate("last_day").toLocalDate()));
      }
      return campaigns;
    } catch (SQLException e) {
      throw new IllegalStateException("Could not load campaigns", e);
    }
  }
}
