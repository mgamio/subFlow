# The one-page design review

From *A Practical Guide to Building Software That Can Change* (2nd edition), Appendix A, by Moises Gamio.
Use it before you start a change, before you send a pull request, or while you
review someone else's design. A "no" is not always a defect: it is a reason to
stop and decide on purpose. The numbers are the chapters that explain each question.

## Before you design

- [ ] Can I state the change, who asked for it, and its rules in their words? (9, 11)
- [ ] Is every important quality requirement a scenario with a measure? (7)
- [ ] Are the constraints and my assumptions written down? (7, 9)

## The model

- [ ] Does each object protect its own rules, with no setter that skips them? (2)
- [ ] Do values from outside become validated value objects at the boundary? (6)
- [ ] Is every piece of data owned by exactly one module or system? (7, 9)

## The code

- [ ] Does each business rule have one home? (Same knowledge, not just similar text.) (1)
- [ ] Does each class change for one actor only? (3)
- [ ] Can the change that keeps coming back be made by adding code? (3)
- [ ] Is business code free of vendor, framework, HTTP and SQL types? (1, 3)
- [ ] Is each abstraction based on real cases, not imagined ones? (1, 2, 3)

## Boundaries and failures

- [ ] Have I decided what happens when each external call fails, times out or is retried? (4, 9)
- [ ] Is every operation that changes data idempotent? (4, 6)
- [ ] Can the API and the schema change while old clients and old code still run? (6)
- [ ] Does every operation on a customer’s data know who the caller is? (6)

## Tests

- [ ] Do the tests check observable behavior, with fakes only at the boundaries? (4)
- [ ] Are time and randomness passed in, so no test sleeps? (4)
- [ ] Does every interface with several implementations have a contract test? (4)
- [ ] Are the module rules checked by the build? (7)

## Decisions and communication

- [ ] Did I compare at least two options, including doing nothing, by what each costs? (5, 10)
- [ ] If this is a one-way door, is the decision recorded with its costs? (5)
- [ ] Does each diagram answer a question for a reader I can name? (8)
- [ ] Is the proposal in the listener’s units, with ranges and named assumptions? (10)
- [ ] Do we know who decides, and is the dissent recorded with a revisit trigger? (10)

The complete checklists for every chapter are in Appendix A of the book.
