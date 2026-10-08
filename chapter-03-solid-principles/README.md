# Chapter 3 — SOLID Principles

What changed in SubFlow since Chapter 2:

- **SRP** — the overdue-accounts job was split into `LateFeePolicy` (owned by Finance)
  and `AccessSuspensionPolicy` (owned by Customer Success).
- **OCP** — `Pricing` chooses a policy through a list of `PricingRule`s. A marketing
  campaign is a new `Campaign` passed to a `CampaignRule`; `Pricing` does not change.
- **LSP** — `PaymentRefunder.refund` has an explicit contract: it returns `REFUNDED` or
  `PENDING_MANUAL`, and never throws for a valid payment. CardHub, which cannot refund
  automatically, now honors the contract instead of throwing.
- **ISP** — the large `PaymentGateway` was split into `PaymentCharger`, `PaymentRefunder`
  and `TransactionHistory`, one per kind of client.
- **DIP** — services depend on those interfaces, which live in `com.subflow.service`.
  The PayFast and CardHub adapters in `com.subflow.payment` implement them.
  `SubFlowApplication` wires everything by hand, without a framework.

`com.payfast.sdk` is a **simulated** vendor library, standing in for a real
payment provider's SDK so the examples run without network access.

| Book section | Before | After |
|---|---|---|
| SRP | `examples.ch03.srp.before` | `com.subflow.service.LateFeePolicy`, `AccessSuspensionPolicy` |
| OCP | `examples.ch03.ocp.before` | `com.subflow.domain.Pricing`, `PricingRule`, `CampaignRule` |
| OCP in a real project | `examples.ch03.ocp.ratelimit.before` | `examples.ch03.ocp.ratelimit.after` |
| LSP | `examples.ch03.lsp.before` | `com.subflow.service.PaymentRefunder`, `com.subflow.payment.cardhub` |
| ISP | `examples.ch03.isp.before` | `PaymentCharger`, `PaymentRefunder`, `TransactionHistory` |
| DIP | `examples.ch03.dip.before` | `com.subflow.payment.payfast.PayFastPaymentAdapter`, `com.subflow.app.SubFlowApplication` |

```
mvn -pl chapter-03-solid-principles test
```
