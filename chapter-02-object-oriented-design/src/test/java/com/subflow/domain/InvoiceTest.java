package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceTest {

  @Test
  void totalIsTheSumOfTheLines() {
    Invoice invoice = new Invoice(1, "ana@example.com", List.of(
        new InvoiceLine("PRO plan, monthly", Money.usd("19.99")),
        new InvoiceLine("Extra storage", Money.usd("5.00"))));
    assertEquals(Money.usd("24.99"), invoice.total());
  }

  @Test
  void linesCannotBeChangedFromOutside() {
    List<InvoiceLine> lines = new ArrayList<>();
    lines.add(new InvoiceLine("PRO plan, monthly", Money.usd("19.99")));
    Invoice invoice = new Invoice(1, "ana@example.com", lines);

    lines.add(new InvoiceLine("Sneaky extra", Money.usd("99.00")));

    assertEquals(Money.usd("19.99"), invoice.total());
    assertThrows(UnsupportedOperationException.class,
        () -> invoice.lines().clear());
  }

  @Test
  void anInvoiceLineCannotBeNegative() {
    assertThrows(IllegalArgumentException.class,
        () -> new InvoiceLine("Refund", Money.usd("-5.00")));
  }

  @Test
  void anInvoiceNeedsAtLeastOneLine() {
    assertThrows(IllegalArgumentException.class,
        () -> new Invoice(1, "ana@example.com", List.of()));
  }
}
