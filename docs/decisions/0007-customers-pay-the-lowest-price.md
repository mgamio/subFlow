# 0007. A customer pays the lowest price they qualify for

Date: 2027-02-15 · Status: Accepted

## Context

Marketing asked for student prices: 20% off the list price, monthly or annual.
The review of the first pull request found that, during a campaign, students
would pay more than other customers, because the first matching pricing rule won.
Partners had the same problem: a partner with a 25% discount paid more than
everyone else during a 30% annual campaign.

Options: (a) keep "first matching rule wins" and let marketing order the rules;
(b) the customer pays the lowest price of every offer they qualify for;
(c) offers combine (student and campaign discounts added together).

## Decision

Option (b). `Pricing` collects every offer the customer qualifies for, plus the
standard price, and `LowestPrice` charges the cheapest. Offers never combine.

Student prices go to customers whose email address is at an academic domain
(`StudentRule.ACADEMIC_DOMAINS`). Product accepts that university staff get them
too.

## Consequences

- No customer pays more because they belong to a group with its own offer.
- The order of the pricing rules no longer matters.
- Every price quote evaluates every rule. With a handful of rules, the cost is
  negligible.
- Marketing (dissenting) preferred option (a), because "this campaign beats every
  other offer" is easy to explain in an advertisement. Under (b) it is still true
  whenever the campaign is the cheapest offer, which is what customers see.
- Revisit if more than 5% of new subscriptions get student prices, or if Finance
  asks for combined discounts.
