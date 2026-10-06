package com.subflow.service;

public record CancellationResult(long subscriptionId,
                                 boolean refundDue) { }
