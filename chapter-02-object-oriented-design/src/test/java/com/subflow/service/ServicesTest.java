package com.subflow.service;

import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.partnerCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.Invoice;
import com.subflow.domain.Money;
import com.subflow.domain.Pricing;
import com.subflow.domain.RenewalQuote;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Checkout, invoice and renewal quote always agree, for every kind of customer. */
class ServicesTest {
  private final Pricing pricing = new Pricing();

  @Test
  void partnerSeesTheSamePriceEverywhere() {
    List<Invoice> saved = new ArrayList<>();
    Invoice invoice = new InvoiceService(pricing, saved::add, i -> { })
        .issueInvoice(annualPro(), partnerCustomer());
    RenewalQuote quote = new RenewalService(pricing)
        .quoteFor(annualPro(), partnerCustomer());
    Money checkout = new CheckoutService(pricing).priceFor(
        partnerCustomer(), annualPro().plan(), annualPro().cycle());

    assertEquals(Money.usd("179.91"), invoice.total());
    assertEquals(invoice.total(), quote.price());
    assertEquals(invoice.total(), checkout);
    assertEquals(Money.usd("239.88"), quote.listPrice());
  }
}
