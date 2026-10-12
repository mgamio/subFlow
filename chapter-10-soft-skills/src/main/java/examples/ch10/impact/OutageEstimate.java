package examples.ch10.impact;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

/**
 * Estimates the yearly cost of payment-provider outages for a given time
 * to recover. Customers see failures until SubFlow recovers, or until the
 * outage ends, whichever comes first.
 */
public final class OutageEstimate {
  private static final BigDecimal SECONDS_PER_MINUTE =
      BigDecimal.valueOf(60);

  private OutageEstimate() { }

  public static OutageImpact forYear(List<Duration> outages,
                                     Duration timeToRecover,
                                     Assumptions assumptions) {
    long secondsOfFailures = outages.stream()
        .map(outage -> min(outage, timeToRecover))
        .mapToLong(Duration::toSeconds)
        .sum();
    long failedCheckouts = BigDecimal.valueOf(secondsOfFailures)
        .multiply(BigDecimal.valueOf(assumptions.checkoutsPerMinute()))
        .divide(SECONDS_PER_MINUTE, 0, RoundingMode.HALF_UP)
        .longValueExact();
    long lostSubscriptions = BigDecimal.valueOf(failedCheckouts)
        .multiply(assumptions.shareWhoNeverReturn())
        .setScale(0, RoundingMode.HALF_UP)
        .longValueExact();
    return new OutageImpact(failedCheckouts, lostSubscriptions,
        assumptions.firstYearValue()
            .times(BigDecimal.valueOf(lostSubscriptions)));
  }

  private static Duration min(Duration a, Duration b) {
    return a.compareTo(b) <= 0 ? a : b;
  }
}
