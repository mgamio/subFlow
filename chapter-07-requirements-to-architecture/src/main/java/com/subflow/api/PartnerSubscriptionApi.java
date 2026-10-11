package com.subflow.api;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.service.SignupService;
import java.util.Optional;

public class PartnerSubscriptionApi {
  private final SignupService signups;
  private final IdempotencyStore idempotency;

  public PartnerSubscriptionApi(SignupService signups,
                                IdempotencyStore idempotency) {
    this.signups = signups;
    this.idempotency = idempotency;
  }

  // Handles: POST /partner/subscriptions   (header: Idempotency-Key)
  @SuppressWarnings("unchecked")
  public ApiResponse<SubscriptionResponse> create(
      String idempotencyKey, CreateSubscriptionRequest request) {
    if (idempotencyKey == null || idempotencyKey.isBlank()) {
      return ApiResponse.failed(ApiErrors.invalidRequest(
          "The Idempotency-Key header is required."));
    }
    Optional<IdempotencyStore.Stored> previous = idempotency.find(idempotencyKey);
    if (previous.isPresent()) {
      if (!previous.get().request().equals(request)) {
        return ApiResponse.failed(new ApiError(
            "https://api.subflow.example/errors/idempotency-key-reused",
            "Idempotency key reused", 422,
            "This key was already used for a different request."));
      }
      return (ApiResponse<SubscriptionResponse>) previous.get().response();
    }

    ApiResponse<SubscriptionResponse> response = handle(request);
    idempotency.save(idempotencyKey, request, response);
    return response;
  }

  private ApiResponse<SubscriptionResponse> handle(CreateSubscriptionRequest request) {
    Plan plan;
    BillingCycle cycle;
    try {
      plan = Plan.valueOf(request.plan());
      cycle = BillingCycle.valueOf(request.cycle());
    } catch (IllegalArgumentException | NullPointerException e) {
      return ApiResponse.failed(ApiErrors.invalidRequest(
          "plan must be BASIC or PRO; cycle must be MONTHLY or ANNUAL."));
    }
    try {
      Subscription created = signups.signUp(request.customerId(), plan, cycle);
      return ApiResponse.created(SubscriptionResponse.from(created));
    } catch (RuntimeException e) {
      return ApiResponse.failed(ApiErrors.from(e));
    }
  }
}
