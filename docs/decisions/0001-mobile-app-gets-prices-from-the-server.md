# 0001. The mobile app gets prices from the server

Date: 2026-11-02 · Status: Accepted

## Context

The new mobile team needs to show plan prices. They proposed reusing SubFlow's
pricing classes, either as a shared library or as a copy. Prices change with
partner agreements and marketing campaigns, often several times a month. The mobile
app is released through app stores, roughly every four weeks, and many users do
not update for months.

## Decision

The mobile app does not calculate prices. It calls `GET /customers/{id}/prices`,
served by `PriceQuoteService`, which uses the same `Pricing` as checkout.

## Consequences

- The price shown is always the price charged, in every app version.
- A campaign starts without a mobile release.
- The app needs a network connection to show prices; it may cache the last answer
  and must show it as "may be out of date".
- Pricing stays a single source of knowledge (DRY) without coupling two release
  schedules (independent evolution).
