package com.payfast.sdk;

/** Part of a SIMULATED vendor SDK used by the book's examples. */
public record PayFastChargeRequest(String customerReference,
                                   long amountInMinorUnits,
                                   String currency) { }
