package com.subflow.app;

import com.payfast.sdk.PayFastClient;
import com.subflow.domain.PaymentProvider;
import com.subflow.domain.Pricing;
import com.subflow.payment.CircuitBreaker;
import com.subflow.payment.FailoverPaymentCharger;
import com.subflow.payment.RetryPolicy;
import com.subflow.payment.RetryingPaymentCharger;
import com.subflow.payment.RoutingPaymentRefunder;
import com.subflow.payment.Sleeper;
import com.subflow.payment.cardhub.CardHubPaymentAdapter;
import com.subflow.payment.cardhub.ManualRefundQueue;
import com.subflow.payment.payfast.PayFastPaymentAdapter;
import com.subflow.service.CampaignCatalog;
import com.subflow.service.CancellationService;
import com.subflow.service.CatalogPricingRules;
import com.subflow.service.CheckoutService;
import com.subflow.service.Outbox;
import com.subflow.service.PaymentRepository;
import com.subflow.service.ProcessedEvents;
import com.subflow.service.RefundOnCancellation;
import com.subflow.service.SubscriptionRepository;
import com.subflow.service.TransactionRunner;
import java.time.Clock;
import java.time.Duration;
import java.util.Map;

/**
 * The composition root. Every instance is stateless apart from short
 * caches, so the load balancer can send any request to any instance.
 */
public class SubFlowApplication {
  static final Duration PRICING_REFRESH = Duration.ofSeconds(60);
  static final int BREAKER_FAILURES = 5;
  static final Duration BREAKER_PAUSE = Duration.ofSeconds(30);

  private final CheckoutService checkout;
  private final CancellationService cancellation;
  private final RefundOnCancellation refunds;
  private final CircuitBreaker payFastBreaker;

  public SubFlowApplication(PayFastClient payFastClient,
                            ManualRefundQueue manualRefunds,
                            SubscriptionRepository subscriptions,
                            PaymentRepository payments,
                            Outbox outbox,
                            ProcessedEvents processedEvents,
                            TransactionRunner transactions,
                            CampaignCatalog campaigns,
                            Clock clock, Sleeper sleeper) {
    Pricing pricing = new Pricing(
        new CatalogPricingRules(campaigns, clock, PRICING_REFRESH), clock);

    PayFastPaymentAdapter payFast =
        new PayFastPaymentAdapter(payFastClient, clock);
    CardHubPaymentAdapter cardHub =
        new CardHubPaymentAdapter(manualRefunds, clock);
    this.payFastBreaker =
        new CircuitBreaker(BREAKER_FAILURES, BREAKER_PAUSE, clock);

    this.checkout = new CheckoutService(pricing,
        new FailoverPaymentCharger(
            new RetryingPaymentCharger(payFast, RetryPolicy.DEFAULT, sleeper),
            cardHub, payFastBreaker),
        payments, clock);
    this.cancellation = new CancellationService(subscriptions, outbox,
        transactions, clock);
    this.refunds = new RefundOnCancellation(payments,
        new RoutingPaymentRefunder(Map.of(
            PaymentProvider.PAYFAST, payFast,
            PaymentProvider.CARDHUB, cardHub)),
        processedEvents);
  }

  public CheckoutService checkout() { return checkout; }
  public CancellationService cancellation() { return cancellation; }
  public RefundOnCancellation refunds() { return refunds; }

  /** Exposed for monitoring: an open breaker should alert someone. */
  public CircuitBreaker.State payFastState() { return payFastBreaker.state(); }
}
