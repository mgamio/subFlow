package com.subflow.api;

/** The notification PayFast sends when a charge succeeds. */
public record PayFastWebhook(String eventId, String type, String chargeId,
                             long subscriptionId, long amountInMinorUnits,
                             String currency) { }
