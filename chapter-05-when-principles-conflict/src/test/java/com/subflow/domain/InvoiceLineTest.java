package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InvoiceLineTest {
  @Test
  void linesAreSubscriptionLinesUnlessSaidOtherwise() {
    assertEquals(InvoiceLine.Kind.SUBSCRIPTION,
        new InvoiceLine("PRO plan, monthly", Money.usd("19.99")).kind());
  }
}
