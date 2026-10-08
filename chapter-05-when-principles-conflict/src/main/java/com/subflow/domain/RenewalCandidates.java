package com.subflow.domain;

import java.time.LocalDate;
import java.time.Month;

/**
 * The cheap test a database can run: same month and day as the start.
 * Mirrors the SQL in JdbcRenewalQueries.
 */
public final class RenewalCandidates {
  private RenewalCandidates() { }

  public static boolean mayRenewOn(LocalDate startDate, LocalDate renewalDate) {
    if (startDate.getMonth() != renewalDate.getMonth()) {
      return false;
    }
    if (startDate.getDayOfMonth() == renewalDate.getDayOfMonth()) {
      return true;
    }
    // Started on Feb 29: renews on Feb 28 in years without Feb 29.
    return renewalDate.getMonth() == Month.FEBRUARY
        && renewalDate.getDayOfMonth() == 28
        && startDate.getDayOfMonth() == 29;
  }
}
