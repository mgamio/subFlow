package com.subflow.domain;

/** Equal today, but different knowledge: see Chapter 1, DRY. */
public final class BillingPeriods {
  public static final int FREE_TRIAL_DAYS = 14;
  public static final int REFUND_WINDOW_DAYS = 14;

  private BillingPeriods() { }
}
