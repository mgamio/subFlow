package examples.ch09.suppliers;

import java.util.List;

public record Page<T>(List<T> items, boolean hasMore) {
  public Page {
    items = List.copyOf(items);
  }
}
