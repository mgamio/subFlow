package com.subflow.testing;

import com.subflow.api.ApiResponse;
import com.subflow.api.IdempotencyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryIdempotencyStore implements IdempotencyStore {
  private final Map<String, Stored> entries = new HashMap<>();

  @Override
  public Optional<Stored> find(String key) {
    return Optional.ofNullable(entries.get(key));
  }

  @Override
  public void save(String key, Object request, ApiResponse<?> response) {
    entries.put(key, new Stored(request, response));
  }
}
