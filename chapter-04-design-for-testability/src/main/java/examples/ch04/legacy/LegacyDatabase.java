package examples.ch04.legacy;

import com.subflow.domain.Subscription;
import java.util.List;

/** Stands in for a static data-access helper found in many old systems. */
public final class LegacyDatabase {
  private LegacyDatabase() { }

  public static List<Subscription> findAllSubscriptions() {
    throw new IllegalStateException("No database in this environment");
  }

  public static String findCustomerEmail(long customerId) {
    throw new IllegalStateException("No database in this environment");
  }
}
