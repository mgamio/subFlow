package com.subflow.domain;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/** A value object: two addresses are equal when their values are equal. */
public record EmailAddress(String value) {
  private static final Pattern SHAPE =
      Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  public EmailAddress {
    Objects.requireNonNull(value, "email");
    value = value.strip().toLowerCase(Locale.ROOT);
    if (!SHAPE.matcher(value).matches()) {
      throw new IllegalArgumentException("Not an email address: " + value);
    }
  }

  public static EmailAddress of(String value) {
    return new EmailAddress(value);
  }
}
