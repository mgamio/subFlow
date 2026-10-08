package examples.ch04.determinism.before;

import com.subflow.domain.Customer;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.Pricing;
import com.subflow.domain.Subscription;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentCharger;
import java.time.LocalDate;

/** The key depends on a clock nobody can control. */
public class CheckoutService {
  private final Pricing pricing;
  private final PaymentCharger payments;

  public CheckoutService(Pricing pricing, PaymentCharger payments) {
    this.pricing = pricing;
    this.payments = payments;
  }

  public Payment pay(Customer customer, Subscription subscription) {
    Money price = pricing.forCustomer(customer)
        .priceFor(subscription.plan(), subscription.cycle());
    String key = "subscription-" + subscription.id()
        + "-" + LocalDate.now();
    return payments.charge(
        new ChargeRequest(subscription.id(), price, key));
  }
}
