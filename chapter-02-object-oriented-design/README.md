# Chapter 2 — Object-Oriented Design: Protecting Your Rules

What changed in SubFlow since Chapter 1:

- `Money` became a real value object: it always has a currency, a correct scale,
  and refuses to add amounts in different currencies.
- `Subscription` validates itself and offers `activate()` and `cancel()` instead of
  the `withStatus(...)` method from Chapter 1, which let any code set any status.
- `Invoice` is composed of `InvoiceLine`s and protects its list from outside changes.
- Partner companies get their own prices. `PricingPolicy` is now an interface with
  two real implementations, `StandardPricing` and `PartnerPricing`, and `Pricing`
  chooses the right one for each customer.

| Book section | Before | After |
|---|---|---|
| Encapsulation and invariants | `examples.ch02.encapsulation.before` | `com.subflow.domain.Money`, `Subscription`, `Invoice` |
| Inheritance and its limits | `examples.ch02.inheritance.before` | `com.subflow.domain.PartnerPricing`, `PartnerAgreement` |
| Polymorphism | `examples.ch02.polymorphism.before` | `com.subflow.domain.Pricing` |
| Relationships among classes | — | `com.subflow.domain.Invoice`, `InvoiceLine` |

```
mvn -pl chapter-02-object-oriented-design test
```
