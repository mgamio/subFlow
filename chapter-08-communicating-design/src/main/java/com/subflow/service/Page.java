package com.subflow.service;

import java.util.List;
import java.util.Optional;

/** One page of results, and where the next page starts (if any). */
public record Page<T>(List<T> items, Optional<String> nextCursor) {
  public Page {
    items = List.copyOf(items);
  }
}
