package examples.ch04.doubles;

import static com.subflow.TestData.START;
import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.PartnerRule;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.domain.Pricing;
import com.subflow.service.CheckoutService;
import com.subflow.service.PaymentCharger;
import com.subflow.service.PaymentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Brittle on purpose: it checks how CheckoutService works, step by step. */
class CheckoutInteractionTest {

  @Test
  void payChargesThenSaves() {
    List<String> calls = new ArrayList<>();
    PaymentCharger charger = request -> {
      calls.add("charge " + request.amount() + " " + request.idempotencyKey());
      return new Payment("pf_1", 7, PaymentProvider.PAYFAST, request.amount(), START);
    };
    PaymentRepository records = new PaymentRepository() {
      public void save(Payment payment) {
        calls.add("save " + payment.id());
      }
      public Optional<Payment> lastPaymentFor(long subscriptionId) {
        calls.add("lastPaymentFor " + subscriptionId);
        return Optional.empty();
      }
    };
    Pricing pricing = new Pricing(List.of(new PartnerRule()), clockOn(START));

    new CheckoutService(pricing, charger, records, clockOn(START))
        .pay(regularCustomer(), annualPro());

    assertEquals(List.of(
        "charge USD 203.90 subscription-7-period-2026-01-01",
        "save pf_1"), calls);
  }
}
