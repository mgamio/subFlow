package com.subflow.domain;

import java.math.BigDecimal;
import java.util.Currency;

/** Recorded early on purpose: see "YAGNI - When not to apply it". */
public record Money(BigDecimal amount, Currency currency) { }
