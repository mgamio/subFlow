package com.subflow.api;

/** What a partner sends. Values are text until the boundary checks them. */
public record CreateSubscriptionRequest(long customerId, String plan, String cycle) { }
