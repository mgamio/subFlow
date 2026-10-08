package com.subflow.notification;

import com.subflow.domain.Invoice;
import com.subflow.service.InvoiceNotifier;
import java.util.List;

/** Sends an invoice through every configured channel. */
public class CompositeInvoiceNotifier implements InvoiceNotifier {
  private final List<InvoiceNotifier> channels;

  public CompositeInvoiceNotifier(List<InvoiceNotifier> channels) {
    this.channels = List.copyOf(channels);
  }

  @Override
  public void send(Invoice invoice) {
    for (InvoiceNotifier channel : channels) {
      channel.send(invoice);
    }
  }
}
