package com.subflow.service;

import java.util.List;

/** Owned by Customer Success. */
public class AccessSuspensionPolicy {
  static final int DAYS_BEFORE_SUSPENSION = 7;

  private final AccessControl access;

  public AccessSuspensionPolicy(AccessControl access) {
    this.access = access;
  }

  public void apply(List<OverdueAccount> accounts) {
    for (OverdueAccount account : accounts) {
      if (account.daysOverdue() >= DAYS_BEFORE_SUSPENSION) {
        access.suspend(account.customerId());
      }
    }
  }
}
