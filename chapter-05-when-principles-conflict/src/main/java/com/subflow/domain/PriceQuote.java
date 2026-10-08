package com.subflow.domain;

/** What a customer would pay today for a plan and cycle, and the list price. */
public record PriceQuote(Plan plan, BillingCycle cycle,
                         Money listPrice, Money price) { }
