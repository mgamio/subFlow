package com.subflow.web;

import com.subflow.service.CancellationResult;
import com.subflow.service.CancellationService;

public class SubscriptionController {
  private final CancellationService cancellations;

  public SubscriptionController(
      CancellationService cancellations) {
    this.cancellations = cancellations;
  }

  // Handles: POST /subscriptions/{id}/cancel
  public CancellationResult cancel(long subscriptionId) {
    return cancellations.cancel(subscriptionId);
  }
}
