package com.subflow.api;

import java.util.Optional;

/** Remembers the response to each idempotency key. */
public interface IdempotencyStore {
  record Stored(Object request, ApiResponse<?> response) { }

  Optional<Stored> find(String key);

  void save(String key, Object request, ApiResponse<?> response);
}
