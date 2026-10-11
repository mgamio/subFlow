package com.subflow.service;

import com.subflow.domain.Invoice;

public interface InvoiceNotifier {
  void send(Invoice invoice);
}
