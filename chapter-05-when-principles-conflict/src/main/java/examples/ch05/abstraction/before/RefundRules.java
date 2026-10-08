package examples.ch05.abstraction.before;

import static com.subflow.domain.BillingPeriods.REFUND_WINDOW_DAYS;

/** The refund rule, expressed through the general rule engine. */
public final class RefundRules {
  private RefundRules() { }

  public static final Specification<CancellationRequest> AFTER_WINDOW =
      r -> r.today().isAfter(
          r.subscription().startDate().plusDays(REFUND_WINDOW_DAYS));

  public static final Specification<CancellationRequest> REFUND_DUE =
      AFTER_WINDOW.not();
}
