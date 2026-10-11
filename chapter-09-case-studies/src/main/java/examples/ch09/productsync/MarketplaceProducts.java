package examples.ch09.productsync;

import java.time.LocalDate;
import java.util.List;

/**
 * The retailer's products that the marketplace sells on a given day. In production,
 * the database function the DBA wrote ("product data by retailer").
 */
public interface MarketplaceProducts {
  List<String> listedProductIds(String retailerId, LocalDate day);
}
