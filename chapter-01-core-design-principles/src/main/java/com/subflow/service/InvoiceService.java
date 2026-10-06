package com.subflow.service;

import com.subflow.domain.Customer;
import com.subflow.domain.Invoice;
import com.subflow.domain.PricingPolicy;
import com.subflow.domain.Subscription;
import java.math.BigDecimal;

public class InvoiceService {
  private final PricingPolicy pricing;
  private final InvoiceRepository invoices;
  private final InvoiceNotifier notifier;

  public InvoiceService(PricingPolicy pricing,
                        InvoiceRepository invoices,
                        InvoiceNotifier notifier) {
    this.pricing = pricing;
    this.invoices = invoices;
    this.notifier = notifier;
  }

  public BigDecimal amountDue(Subscription subscription) {
    return pricing.priceFor(subscription.plan(),
                            subscription.cycle());
  }

  public Invoice issueInvoice(Subscription subscription,
                              Customer customer) {
    Invoice invoice = new Invoice(
        subscription.id(), customer.email(),
        amountDue(subscription));
    invoices.save(invoice);
    notifier.send(invoice);
    return invoice;
  }
}
