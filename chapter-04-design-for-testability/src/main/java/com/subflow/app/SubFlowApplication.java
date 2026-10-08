package com.subflow.app;

import com.payfast.sdk.PayFastClient;
import com.subflow.domain.Campaign;
import com.subflow.domain.CampaignRule;
import com.subflow.domain.PartnerRule;
import com.subflow.domain.PaymentProvider;
import com.subflow.domain.Pricing;
import com.subflow.domain.PricingRule;
import com.subflow.payment.RetryPolicy;
import com.subflow.payment.RetryingPaymentCharger;
import com.subflow.payment.RoutingPaymentRefunder;
import com.subflow.payment.Sleeper;
import com.subflow.payment.cardhub.CardHubPaymentAdapter;
import com.subflow.payment.cardhub.ManualRefundQueue;
import com.subflow.payment.payfast.PayFastPaymentAdapter;
import com.subflow.service.CancellationService;
import com.subflow.service.CheckoutService;
import com.subflow.service.PaymentRepository;
import com.subflow.service.SubscriptionRepository;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The composition root: the one place that knows every concrete
 * class and connects them. No framework is needed.
 */
public class SubFlowApplication {
  private final CheckoutService checkout;
  private final CancellationService cancellation;

  public SubFlowApplication(PayFastClient payFastClient,
                            ManualRefundQueue manualRefunds,
                            SubscriptionRepository subscriptions,
                            PaymentRepository payments,
                            List<Campaign> campaigns,
                            Clock clock, Sleeper sleeper) {
    List<PricingRule> rules = new ArrayList<>();
    rules.add(new PartnerRule());          // partners first
    for (Campaign campaign : campaigns) {
      rules.add(new CampaignRule(campaign));
    }
    Pricing pricing = new Pricing(rules, clock);

    PayFastPaymentAdapter payFast =
        new PayFastPaymentAdapter(payFastClient, clock);
    CardHubPaymentAdapter cardHub =
        new CardHubPaymentAdapter(manualRefunds, clock);

    this.checkout = new CheckoutService(pricing,
        new RetryingPaymentCharger(payFast, RetryPolicy.DEFAULT, sleeper),
        payments, clock);
    this.cancellation = new CancellationService(subscriptions,
        payments,
        new RoutingPaymentRefunder(Map.of(
            PaymentProvider.PAYFAST, payFast,
            PaymentProvider.CARDHUB, cardHub)),
        clock);
  }

  public CheckoutService checkout() { return checkout; }
  public CancellationService cancellation() { return cancellation; }
}
