package examples.ch04.determinism;

import static com.subflow.TestData.START;
import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.PartnerRule;
import com.subflow.domain.Pricing;
import com.subflow.service.CheckoutService;
import com.subflow.testing.FakePaymentCharger;
import com.subflow.testing.InMemoryPayments;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class HiddenClockTest {

  @Test
  void aRetryAcrossMidnightKeepsTheSameKey() {
    Pricing pricing = new Pricing(List.of(new PartnerRule()), clockOn(START));
    FakePaymentCharger payFast = new FakePaymentCharger(START);
    InMemoryPayments records = new InMemoryPayments();
    LocalDate march3 = LocalDate.of(2026, 3, 3);

    new CheckoutService(pricing, payFast, records, at(march3.atTime(23, 59, 59)))
        .pay(regularCustomer(), annualPro());
    new CheckoutService(pricing, payFast, records, at(march3.plusDays(1).atStartOfDay()))
        .pay(regularCustomer(), annualPro());

    assertEquals(1, payFast.charges().size());
  }

  private static Clock at(LocalDateTime time) {
    return Clock.fixed(time.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
  }
}
