# 0002. Record invoice line kinds before VAT

Date: 2026-11-09 · Status: Accepted

## Context

Finance has announced that SubFlow will charge VAT in the European Union next
quarter. The VAT rules (rates per country, reverse charge for businesses) are not
yet defined. Invoices are legal records and are kept for ten years.

## Decision

Every `InvoiceLine` records its kind (`SUBSCRIPTION`, `TAX`, `ADJUSTMENT`) from
now on. We do not build a tax calculation yet.

## Consequences

- When VAT starts, old and new invoices have the same structure; no migration of
  stored invoices.
- The tax engine will be designed when the rules are known (YAGNI for the feature).
- One more field to maintain in a part of the code that does not use it yet.
