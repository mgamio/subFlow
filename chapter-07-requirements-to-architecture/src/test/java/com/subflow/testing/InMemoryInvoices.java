package com.subflow.testing;

import com.subflow.persistence.InvoiceCursor;
import com.subflow.service.InvoiceQueries;
import com.subflow.service.InvoiceSummary;
import com.subflow.service.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Same keyset logic as JdbcInvoiceQueries, over a list. */
public class InMemoryInvoices implements InvoiceQueries {
  private final List<InvoiceSummary> invoices = new ArrayList<>();
  private final Map<Long, Long> customerOfSubscription;

  public InMemoryInvoices(Map<Long, Long> customerOfSubscription) {
    this.customerOfSubscription = customerOfSubscription;
  }

  public void add(InvoiceSummary invoice) {
    invoices.add(invoice);
  }

  @Override
  public Page<InvoiceSummary> invoicesOf(long customerId,
      Optional<String> cursor, int limit) {
    long afterId = cursor.map(InvoiceCursor::decode).orElse(0L);
    List<InvoiceSummary> rows = invoices.stream()
        .filter(i -> customerOfSubscription.get(i.subscriptionId()) == customerId)
        .filter(i -> i.id() > afterId)
        .sorted((a, b) -> Long.compare(a.id(), b.id()))
        .limit(limit + 1L)
        .toList();
    return InvoiceCursor.page(rows, limit);
  }

  /** Newest first: what the first version of the invoice page used. */
  public List<InvoiceSummary> allNewestFirst() {
    List<InvoiceSummary> sorted = new ArrayList<>(invoices);
    sorted.sort((a, b) -> Long.compare(b.id(), a.id()));
    return sorted;
  }
}
