package com.subflow.service;

/** The refund, if due, is requested asynchronously. */
public record CancellationResult(long subscriptionId, boolean refundDue) { }
