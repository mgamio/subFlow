package com.subflow.domain;

import java.util.Optional;

public record Customer(long id, String name, String email,
                       PartnerAgreement partnerAgreement) {

  public Customer {
    email = EmailAddress.of(email).value();   // always normalized
  }

  /** A regular customer, without a partner agreement. */
  public Customer(long id, String name, String email) {
    this(id, name, email, null);
  }

  public Optional<PartnerAgreement> partner() {
    return Optional.ofNullable(partnerAgreement);
  }
}
