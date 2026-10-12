# Capstone: family plans

The last exercise of the book (Chapter 11). Start from `chapter-10-soft-skills`,
which is SubFlow at the end of the book. There is no solution in this repository:
the point is to make the decisions yourself, and to be able to explain them.

## The request

> Marketing wants family plans. One customer pays; up to five family members
> use SubFlow with their own login. The payer receives one invoice a month.
> A family plan costs the plan price plus 40% for each extra member.

## Step 1: read and ask (Chapter 9)

List the nouns, the verbs and the rules. Then write down your questions. Some
that change the design:

- What happens to the members when the payer cancels, or when a payment fails?
  (Chapter 3: `AccessSuspensionPolicy`.)
- Can a member be a partner employee or a student? Does the family price compete
  with their own offers? (ADR 0007: a customer pays the lowest price they qualify
  for; offers never combine.)
- Can a member leave the family and keep a subscription of their own?
- Can a member see the payer's invoices? (Chapter 6: authorization at the boundary.)

Write the answers you assume, and mark them as assumptions.

## Step 2: design and decide

- Model: is a family a new entity, or a kind of subscription? Where is the
  invariant "at most five members" protected? (Chapters 2 and 6)
- Pricing: is the family price a `PricingRule`, a `PricingPolicy`, or neither?
  (Chapter 3)
- Invoices: one line per extra member? Which `InvoiceLine.Kind`? (ADR 0002)
- Data: write the migration with expand and contract. (Chapter 6)
- API: how does a partner add a member, safely retried? (Chapter 6: idempotency)
- Record at least one decision as `docs/decisions/0008-...md`, with the options
  you rejected. (Chapters 5 and 7)

## Step 3: prove it (Chapter 4)

- Tests for the invariant, the price and the cancellation rule, with fakes only
  at the boundaries.
- Update `docs/diagrams/subscription-states.mmd` if the life cycle changes, and
  keep `SubscriptionStateDiagramTest` passing. (Chapter 8)
- `ArchitectureRulesTest` and `ModuleDiagramTest` must still pass. (Chapters 7 and 8)

## Step 4: explain it (Chapter 10)

Write the one-page memo for the head of product: what you need, what it costs,
what you give up, and what you do not know yet.

## Done when

- [ ] Every rule in the request has a test.
- [ ] Every assumption is written down.
- [ ] At least one decision record explains a choice that is costly to reverse.
- [ ] The diagrams and the code agree.
- [ ] Someone else has reviewed it, using the checklists in Appendix A.
