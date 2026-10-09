package com.subflow.api;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.subflow.service.SignupService;
import com.subflow.testing.InMemoryIdempotencyStore;
import com.subflow.testing.InMemorySubscriptions;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

class PartnerSubscriptionApiTest {
  private final InMemorySubscriptions subscriptions = new InMemorySubscriptions();
  private final AtomicLong ids = new AtomicLong(100);
  private final SignupService signups =
      new SignupService(subscriptions, ids::incrementAndGet, clockOn(START));
  private final PartnerSubscriptionApi api =
      new PartnerSubscriptionApi(signups, new InMemoryIdempotencyStore());
  private final CreateSubscriptionRequest request =
      new CreateSubscriptionRequest(42, "PRO", "ANNUAL");

  @Test
  void theOldApiCreatesADuplicateOnRetry() {
    var old = new examples.ch06.idempotency.before.PartnerSubscriptionApi(signups);
    old.create(request);
    old.create(request);      // the partner's retry after a timeout
    assertEquals(2, subscriptions.all().size());
  }

  @Test
  void aRetryWithTheSameKeyReturnsTheOriginalSubscription() {
    ApiResponse<SubscriptionResponse> first = api.create("acme-order-7781", request);
    ApiResponse<SubscriptionResponse> retry = api.create("acme-order-7781", request);

    assertEquals(201, first.status());
    assertSame(first, retry);
    assertEquals(1, subscriptions.all().size());
  }

  @Test
  void reusingAKeyForADifferentRequestIsRejected() {
    api.create("acme-order-7781", request);
    ApiResponse<SubscriptionResponse> other = api.create("acme-order-7781",
        new CreateSubscriptionRequest(43, "BASIC", "MONTHLY"));
    assertEquals(422, other.status());
    assertEquals(1, subscriptions.all().size());
  }

  @Test
  void aMissingKeyOrAnUnknownPlanIsAClearClientError() {
    assertEquals(400, api.create(null, request).status());
    ApiResponse<SubscriptionResponse> bad =
        api.create("k-1", new CreateSubscriptionRequest(42, "GOLD", "ANNUAL"));
    assertEquals(400, bad.status());
    assertEquals("Invalid request", bad.error().title());
  }
}
