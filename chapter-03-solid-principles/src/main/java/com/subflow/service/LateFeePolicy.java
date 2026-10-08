package com.subflow.service;

import java.util.List;

/** Owned by Finance. */
public class LateFeePolicy {
  static final int DAYS_BEFORE_LATE_FEE = 14;

  private final LateFees lateFees;

  public LateFeePolicy(LateFees lateFees) {
    this.lateFees = lateFees;
  }

  public void apply(List<OverdueAccount> accounts) {
    for (OverdueAccount account : accounts) {
      if (account.daysOverdue() >= DAYS_BEFORE_LATE_FEE) {
        lateFees.charge(account.customerId());
      }
    }
  }
}
