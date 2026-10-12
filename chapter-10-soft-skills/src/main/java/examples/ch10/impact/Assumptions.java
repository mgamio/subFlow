package examples.ch10.impact;

import com.subflow.domain.Money;
import java.math.BigDecimal;

/**
 * The numbers behind an estimate, each one named so that anyone can
 * challenge it. Most come from the product team, not from developers.
 */
public record Assumptions(
    int checkoutsPerMinute,         // average over a day
    BigDecimal shareWhoNeverReturn, // after a failed checkout
    Money firstYearValue) {         // of one new subscription

  public Assumptions {
    if (checkoutsPerMinute < 0) {
      throw new IllegalArgumentException("Checkouts cannot be negative");
    }
    if (shareWhoNeverReturn.signum() < 0
        || shareWhoNeverReturn.compareTo(BigDecimal.ONE) > 0) {
      throw new IllegalArgumentException("A share is between 0 and 1");
    }
  }

  public Assumptions withShareWhoNeverReturn(String share) {
    return new Assumptions(checkoutsPerMinute, new BigDecimal(share),
        firstYearValue);
  }
}
