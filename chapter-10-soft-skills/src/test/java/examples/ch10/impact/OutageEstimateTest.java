package examples.ch10.impact;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.Money;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

class OutageEstimateTest {
  // PayFast's outages last year (ADR 0005)
  static final List<Duration> LAST_YEAR =
      List.of(Duration.ofMinutes(47), Duration.ofMinutes(12));
  static final Duration WAIT_FOR_PAYFAST = Duration.ofDays(1);
  static final Duration MANUAL_SWITCH = Duration.ofMinutes(10);
  static final Duration AUTOMATIC_FAILOVER = Duration.ofMinutes(1);

  static final Assumptions PRODUCT_TEAM = new Assumptions(
      30, new BigDecimal("0.25"), Money.usd("140"));

  @Test
  void waitingForPayFastCostsAboutSixtyThousandDollarsAYear() {
    OutageImpact today =
        OutageEstimate.forYear(LAST_YEAR, WAIT_FOR_PAYFAST, PRODUCT_TEAM);

    assertEquals(1770, today.failedCheckouts());
    assertEquals(443, today.lostSubscriptions());
    assertEquals(Money.usd("62020"), today.lostRevenue());
  }

  @Test
  void eachOptionInTheBusinessUnits() {
    OutageImpact manual =
        OutageEstimate.forYear(LAST_YEAR, MANUAL_SWITCH, PRODUCT_TEAM);
    OutageImpact automatic =
        OutageEstimate.forYear(LAST_YEAR, AUTOMATIC_FAILOVER, PRODUCT_TEAM);

    assertEquals(new OutageImpact(600, 150, Money.usd("21000")), manual);
    assertEquals(new OutageImpact(60, 15, Money.usd("2100")), automatic);
  }

  @Test
  void anUncertainAssumptionBecomesARange() {
    Assumptions optimistic = PRODUCT_TEAM.withShareWhoNeverReturn("0.10");
    Assumptions pessimistic = PRODUCT_TEAM.withShareWhoNeverReturn("0.40");

    assertEquals(Money.usd("24780"), OutageEstimate
        .forYear(LAST_YEAR, WAIT_FOR_PAYFAST, optimistic).lostRevenue());
    assertEquals(Money.usd("99120"), OutageEstimate
        .forYear(LAST_YEAR, WAIT_FOR_PAYFAST, pessimistic).lostRevenue());
  }
}
