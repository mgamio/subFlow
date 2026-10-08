# Chapter 5 — When Principles Conflict

Each conflict in the chapter ends with a decision. Where the decision changes code,
it is here; three of them are also recorded in `docs/decisions/` at the repository root.

| Conflict | Decision in code |
|---|---|
| Reuse vs independent evolution | `PriceQuoteService` + `web.PriceController`: the mobile app asks the server for prices ([ADR 0001](../docs/decisions/0001-mobile-app-gets-prices-from-the-server.md)) |
| YAGNI vs a known requirement | `InvoiceLine.Kind` recorded now; no tax engine yet ([ADR 0002](../docs/decisions/0002-record-invoice-line-kinds-before-vat.md)) |
| Encapsulation vs query convenience | `RevenueQueries` read model; `examples.ch05.queries.before` loads every object |
| Abstraction vs readability | `CancellationService` keeps a plain rule; `examples.ch05.abstraction.before` is the rule engine |
| Performance vs purity | `RenewalQueries.activeAnnualRenewingOn` filters in SQL; the domain rule stays the final check ([ADR 0003](../docs/decisions/0003-filter-renewal-candidates-in-sql.md)) |

```
mvn -pl chapter-05-when-principles-conflict test
```
