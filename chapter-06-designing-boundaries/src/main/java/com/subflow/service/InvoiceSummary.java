package com.subflow.service;

import com.subflow.domain.Money;
import java.time.LocalDate;

public record InvoiceSummary(long id, long subscriptionId,
                             LocalDate issuedOn, Money total) { }
