# 0005. Fail over card charges to CardHub when PayFast is down

Date: 2027-01-18 · Status: Accepted

## Context

The contract requires that checkout recover from a payment-provider outage within
five minutes. PayFast had two outages last year (47 and 12 minutes). CardHub can
also charge cards for all our customers, at a slightly higher fee.

## Decision

Charges go through a circuit breaker around PayFast (`FailoverPaymentCharger`).
After 5 consecutive failures the breaker opens for 30 seconds, and charges go to
CardHub. When PayFast is unreachable (connection refused) a charge fails over
immediately. When the outcome is unknown (timeout), it is **not** sent to CardHub,
because PayFast may already have charged the card.

## Consequences

- Checkout keeps working during a PayFast outage. When PayFast refuses connections,
  every charge fails over at once. When PayFast hangs, each call fails only after
  about 33 seconds (timeouts and retries); at peak (3 checkouts per second) the
  breaker opens about 34 seconds after the outage starts. About 100 checkouts started
  in that time end as "we are confirming your payment", plus one trial checkout
  roughly every minute while the outage lasts.
- At low traffic, most checkouts during an outage are trial calls. A future
  improvement is a health check that tests PayFast without a customer's charge.
- Refunds go to the provider that took the payment (`RoutingPaymentRefunder`), so
  CardHub's manual refunds increase during outages.
- Risk: a customer whose PayFast charge timed out may retry and be charged by
  CardHub too. The daily reconciliation (`TransactionHistory`) flags such pairs
  for an automatic refund.
- Operations must be alerted when the breaker opens.
