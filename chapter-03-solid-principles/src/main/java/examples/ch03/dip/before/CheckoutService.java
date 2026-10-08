package examples.ch03.dip.before;

import com.payfast.sdk.PayFastCharge;
import com.payfast.sdk.PayFastChargeRequest;
import com.payfast.sdk.PayFastClient;
import com.subflow.domain.Customer;
import com.subflow.domain.Money;
import com.subflow.domain.Pricing;
import com.subflow.domain.Subscription;
import java.time.Clock;

/** Business logic that depends directly on a vendor's SDK. */
public class CheckoutService {
  private final Pricing pricing;
  private final PayFastClient payFast = new PayFastClient(
      System.getenv("PAYFAST_API_KEY"), Clock.systemUTC());

  public CheckoutService(Pricing pricing) {
    this.pricing = pricing;
  }

  public PayFastCharge pay(Customer customer,
                           Subscription subscription) {
    Money price = pricing.forCustomer(customer)
        .priceFor(subscription.plan(), subscription.cycle());
    long cents = price.amount().movePointRight(2).longValueExact();
    return payFast.createCharge(new PayFastChargeRequest(
        "subscription-" + subscription.id(), cents, "USD"));
  }
}
