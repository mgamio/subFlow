# Chapter 7 — From Requirements to Architecture

The enterprise contract brings three quality requirements. Each became a scenario,
a decision record and code:

| Scenario | Decision | Code |
|---|---|---|
| 10,000 requests per minute | Modular monolith, stateless instances ([ADR 0004](../docs/decisions/0004-modular-monolith.md)) | `ArchitectureRulesTest` (module rules as a test) |
| Recover from a PayFast outage within 5 minutes | Circuit breaker, fail over to CardHub ([ADR 0005](../docs/decisions/0005-fail-over-to-cardhub.md)) | `payment.CircuitBreaker`, `payment.FailoverPaymentCharger` |
| Change pricing rules without redeploying | Pricing rules as data ([ADR 0006](../docs/decisions/0006-pricing-rules-as-data.md)) | `service.CatalogPricingRules`, `persistence.JdbcCampaignCatalog` |

```
mvn -pl chapter-07-requirements-to-architecture test
```
