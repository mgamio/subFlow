# 0004. Stay a modular monolith, deployed as several stateless instances

Date: 2027-01-11 · Status: Accepted

## Context

The enterprise contract requires 10,000 requests per minute (about 170 per second)
at peak, with 95% of checkout requests answered in under 300 ms. Today SubFlow is
one deployable application, built by one team of six developers. Some developers
proposed splitting it into services (pricing, billing, payments, notifications).
A load test of one instance on the current hardware handled 90 requests per second
within the response-time goal; the database was not the bottleneck.

## Decision

Keep one deployable application, organized in modules with enforced dependency
rules (`ArchitectureRulesTest`). Run at least three stateless instances behind a
load balancer; scale by adding instances. Reports read from a database replica.

## Consequences

- Throughput: three instances give about 270 requests per second, with room for
  one instance to fail. More instances can be added in minutes.
- One deployment, one database transaction for each business operation, no
  network calls between our own modules.
- The module rules keep the option to extract a service later, if a module needs
  a different release cycle, team or scaling profile.
- Every change is deployed together; a defect in one module can affect all.
- Instances must not keep user state in memory (sessions live in the client token).
