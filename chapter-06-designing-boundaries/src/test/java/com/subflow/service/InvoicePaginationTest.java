package com.subflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.subflow.api.ApiResponse;
import com.subflow.api.InvoiceApi;
import com.subflow.domain.Money;
import com.subflow.testing.InMemoryInvoices;
import examples.ch06.pagination.before.OffsetInvoicePages;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class InvoicePaginationTest {
  private final InMemoryInvoices invoices = new InMemoryInvoices(Map.of(7L, 1L, 8L, 2L));

  private void issue(long id, long subscriptionId) {
    invoices.add(new InvoiceSummary(id, subscriptionId,
        LocalDate.of(2026, 1, 1).plusDays(id), Money.usd("19.99")));
  }

  @Test
  void offsetPagesShowAnInvoiceTwiceWhenANewOneArrives() {
    for (long id = 1; id <= 5; id++) issue(id, 7);
    List<InvoiceSummary> page0 = OffsetInvoicePages.page(invoices.allNewestFirst(), 0, 2);
    issue(6, 7);                                          // a new invoice is issued
    List<InvoiceSummary> page1 = OffsetInvoicePages.page(invoices.allNewestFirst(), 1, 2);
    assertEquals(page0.get(1), page1.get(0));             // shown twice
  }

  @Test
  void cursorPagesNeverRepeatOrSkipAnInvoice() {
    for (long id = 1; id <= 45; id++) issue(id, id % 3 == 0 ? 8 : 7);
    InvoiceApi api = new InvoiceApi(invoices);
    List<Long> seen = new ArrayList<>();
    Optional<String> cursor = Optional.empty();
    do {
      ApiResponse<Page<InvoiceSummary>> response =
          api.list(new Caller(1), cursor, Optional.of(7));
      response.body().items().forEach(i -> seen.add(i.id()));
      if (seen.size() == 7) issue(46, 7);                 // arrives while paging
      cursor = response.body().nextCursor();
    } while (cursor.isPresent());

    assertEquals(seen.size(), new HashSet<>(seen).size());
    assertEquals(31, seen.size());                        // 30 of customer 1, plus the new one
    assertTrue(seen.contains(46L));
  }

  @Test
  void limitsAreChecked() {
    InvoiceApi api = new InvoiceApi(invoices);
    assertEquals(400, api.list(new Caller(1), Optional.empty(), Optional.of(500)).status());
    assertEquals(400, api.list(new Caller(1), Optional.of("not-a-cursor"), Optional.empty()).status());
  }
}
