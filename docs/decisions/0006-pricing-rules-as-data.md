# 0006. Pricing rules as data, not as a separate service

Date: 2027-01-25 · Status: Accepted

## Context

Marketing must be able to start a campaign without a deployment of checkout.
Options: (a) campaigns in a database table, read by every instance; (b) a separate
pricing service that checkout calls; (c) keep campaigns in code and deploy more
often.

## Decision

Option (a). Campaigns live in the `campaigns` table, managed by an admin screen.
Each instance reloads them at most every 60 seconds (`CatalogPricingRules`).
Partner agreements remain data on the customer.

## Consequences

- A new campaign is live on every instance within 60 seconds, without a release.
- No new deployable, no network call on the checkout path.
- During those 60 seconds, two instances may show different prices; checkout
  charges the price computed on the instance that handles the payment, and the
  price page may be up to 60 seconds behind.
- Campaigns can only use the shapes the code supports (an annual discount between
  dates). A new kind of rule still needs a release.
