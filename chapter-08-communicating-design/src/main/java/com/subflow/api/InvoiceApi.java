package com.subflow.api;

import com.subflow.service.Caller;
import com.subflow.service.InvoiceQueries;
import com.subflow.service.InvoiceSummary;
import com.subflow.service.Page;
import java.util.Optional;

public class InvoiceApi {
  static final int DEFAULT_LIMIT = 20;
  static final int MAX_LIMIT = 100;

  private final InvoiceQueries invoices;

  public InvoiceApi(InvoiceQueries invoices) {
    this.invoices = invoices;
  }

  // Handles: GET /invoices?limit=20&cursor=...
  public ApiResponse<Page<InvoiceSummary>> list(Caller caller,
      Optional<String> cursor, Optional<Integer> limit) {
    int size = limit.orElse(DEFAULT_LIMIT);
    if (size < 1 || size > MAX_LIMIT) {
      return ApiResponse.failed(ApiErrors.invalidRequest(
          "limit must be between 1 and " + MAX_LIMIT + "."));
    }
    try {
      return ApiResponse.ok(invoices.invoicesOf(caller.customerId(), cursor, size));
    } catch (RuntimeException e) {
      return ApiResponse.failed(ApiErrors.from(e));
    }
  }
}
