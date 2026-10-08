package com.subflow.service;

public class SubscriptionNotFoundException extends RuntimeException {
  public SubscriptionNotFoundException(long subscriptionId) {
    super("Subscription not found: " + subscriptionId);
  }
}
