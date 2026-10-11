package com.payfast.sdk;

import java.time.LocalDate;

/** Part of a SIMULATED vendor SDK used by the book's examples. */
public record PayFastCharge(String id, String customerReference,
                            long amountInMinorUnits, String currency,
                            LocalDate date) { }
