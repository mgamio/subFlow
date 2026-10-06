package com.subflow.service;

import static com.subflow.TestData.annualProSubscription;
import static com.subflow.TestData.customer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.Invoice;
import com.subflow.domain.PricingPolicy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceServiceTest {

  @Test
  void issuesStoresAndSendsTheInvoice() {
    List<Invoice> saved = new ArrayList<>();
    List<Invoice> sent = new ArrayList<>();
    InvoiceService service = new InvoiceService(
        new PricingPolicy(), saved::add, sent::add);

    service.issueInvoice(annualProSubscription(), customer());

    assertEquals(new BigDecimal("203.90"),
                 saved.get(0).amount());
    assertEquals(saved, sent);
  }
}
