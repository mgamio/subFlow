package com.subflow.domain;

import java.math.BigDecimal;

public record Invoice(long subscriptionId,
                      String customerEmail,
                      BigDecimal amount) { }
