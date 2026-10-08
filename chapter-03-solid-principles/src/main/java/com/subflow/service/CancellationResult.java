package com.subflow.service;

/** refund is null when no refund was due. */
public record CancellationResult(long subscriptionId,
                                 RefundResult refund) {
  public boolean refundDue() {
    return refund != null;
  }
}
