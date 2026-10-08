package examples.ch03.lsp.before;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import java.time.LocalDate;

public class CardHubGateway implements PaymentGateway {

  @Override
  public Payment charge(long subscriptionId, Money amount) {
    return new Payment("ch_1", subscriptionId,
        PaymentProvider.CARDHUB, amount, LocalDate.now());
  }

  @Override
  public void refund(Payment payment) {
    throw new UnsupportedOperationException(
        "CardHub refunds are made by hand in the CardHub dashboard");
  }
}
