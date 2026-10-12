package com.subflow.api;

import com.subflow.service.ConcurrentUpdateException;
import com.subflow.service.SubscriptionNotFoundException;

/** Translates what went wrong inside into the API's error contract. */
public final class ApiErrors {
  private static final String BASE = "https://api.subflow.example/errors/";

  private ApiErrors() { }

  public static ApiError invalidRequest(String detail) {
    return new ApiError(BASE + "invalid-request", "Invalid request", 400, detail);
  }

  public static ApiError from(RuntimeException e) {
    if (e instanceof SubscriptionNotFoundException) {
      return new ApiError(BASE + "not-found", "Subscription not found", 404,
          e.getMessage());
    }
    if (e instanceof ConcurrentUpdateException) {
      return new ApiError(BASE + "conflict", "The subscription changed", 409,
          "Reload the subscription and try again.");
    }
    if (e instanceof IllegalStateException) {
      return new ApiError(BASE + "invalid-state", "Not allowed in this state", 409,
          e.getMessage());
    }
    if (e instanceof IllegalArgumentException) {
      return invalidRequest(e.getMessage());
    }
    // Never expose internal details of unexpected failures.
    return new ApiError(BASE + "internal", "Internal error", 500,
        "Something went wrong on our side. Please try again later.");
  }
}
