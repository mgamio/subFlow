package com.subflow.service;

import com.subflow.domain.Invoice;

public interface InvoiceRepository {
  void save(Invoice invoice);
}
