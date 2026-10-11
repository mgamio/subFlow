package com.subflow.service;

import java.util.Optional;

public interface InvoiceQueries {
  /**
   * Invoices of one customer, oldest first, after the given cursor.
   * The cursor is opaque to callers: they only send back what they got.
   */
  Page<InvoiceSummary> invoicesOf(long customerId, Optional<String> cursor, int limit);
}
