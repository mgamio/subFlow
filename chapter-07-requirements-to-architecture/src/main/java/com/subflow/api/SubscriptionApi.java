package com.subflow.api;

import com.subflow.service.CancellationResult;
import com.subflow.service.CancellationService;
import com.subflow.service.Caller;

public class SubscriptionApi {
  private final CancellationService cancellations;

  public SubscriptionApi(CancellationService cancellations) {
    this.cancellations = cancellations;
  }

  // Handles: POST /subscriptions/{id}/cancel   (caller from the session)
  public ApiResponse<CancellationResult> cancel(Caller caller, long subscriptionId) {
    try {
      return ApiResponse.ok(cancellations.cancel(caller, subscriptionId));
    } catch (RuntimeException e) {
      return ApiResponse.failed(ApiErrors.from(e));
    }
  }
}
