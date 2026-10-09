package examples.ch06.idempotency.before;

import com.subflow.api.PayFastWebhook;
import com.subflow.domain.Invoice;
import com.subflow.domain.InvoiceLine;
import com.subflow.domain.Money;
import com.subflow.service.InvoiceRepository;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/** Issues an invoice for every notification, duplicates included. */
public class PaymentWebhookApi {
  private final InvoiceRepository invoices;

  public PaymentWebhookApi(InvoiceRepository invoices) {
    this.invoices = invoices;
  }

  public void receive(PayFastWebhook webhook) {
    Currency currency = Currency.getInstance(webhook.currency());
    Money amount = new Money(BigDecimal.valueOf(webhook.amountInMinorUnits(),
        currency.getDefaultFractionDigits()), currency);
    invoices.save(new Invoice(webhook.subscriptionId(), "unknown@example.com",
        List.of(new InvoiceLine("Subscription", amount))));
  }
}
