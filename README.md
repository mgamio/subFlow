# SubFlow — companion code

Source code for the book **A Practical Guide to Building Software That Can Change:
Software Design Principles, Trade-offs, and Architecture for Working Developers**
(Second Edition) by Moises Gamio — [codersite.dev](https://codersite.dev).

SubFlow is a small subscription-billing service. It starts simple and a little messy,
and grows chapter by chapter as the book introduces new design principles.

## Requirements

- Java 17 or later
- Maven 3.9 or later

## Get the code and run the tests

```
git clone https://github.com/mgamio/subFlow.git
cd subFlow
mvn test
```

## Optional: mutation testing

```
mvn -P mutation -pl chapter-04-design-for-testability test
```

The report is written to `target/pit-reports/index.html` (see Chapter 4).

## How the code is organized

Each chapter is a separate Maven module, so you can compare SubFlow at the end of
any chapter with the next one.

| Module | Book chapter |
|---|---|
| `chapter-01-core-design-principles` | 1. Core Design Principles |
| `chapter-02-object-oriented-design` | 2. Object-Oriented Design: Protecting Your Rules |
| `chapter-03-solid-principles` | 3. SOLID Principles |
| `chapter-04-design-for-testability` | 4. Design for Testability |

Inside each module:

- `com.subflow` — SubFlow as it stands **at the end of the chapter**.
- `examples.chNN.<section>.before` — the problematic code shown in the book,
  kept so you can compare it with the refactored version. It compiles, but it is
  not meant to be reused.

The tests in `src/test/java` are the examples from the book, plus tests that prove
each refactoring kept the original behavior.

## License

MIT — see `LICENSE`.
