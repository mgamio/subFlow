package com.subflow.service;

import com.subflow.domain.Customer;
import com.subflow.domain.Invoice;
import com.subflow.domain.InvoiceLine;
import com.subflow.domain.Money;
import com.subflow.domain.Pricing;
import com.subflow.domain.Subscription;
import java.util.List;

public class InvoiceService {
  private final Pricing pricing;
  private final InvoiceRepository invoices;
  private final InvoiceNotifier notifier;

  public InvoiceService(Pricing pricing,
                        InvoiceRepository invoices,
                        InvoiceNotifier notifier) {
    this.pricing = pricing;
    this.invoices = invoices;
    this.notifier = notifier;
  }

  public Invoice issueInvoice(Subscription subscription,
                              Customer customer) {
    Money price = pricing.forCustomer(customer)
        .priceFor(subscription.plan(), subscription.cycle());
    InvoiceLine line = new InvoiceLine(
        subscription.plan() + " plan, "
            + subscription.cycle().name().toLowerCase(),
        price);
    Invoice invoice = new Invoice(
        subscription.id(), customer.email(), List.of(line));
    invoices.save(invoice);
    notifier.send(invoice);
    return invoice;
  }
}
