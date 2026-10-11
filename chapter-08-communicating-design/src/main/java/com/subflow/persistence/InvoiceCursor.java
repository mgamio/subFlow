package com.subflow.persistence;

import com.subflow.service.InvoiceSummary;
import com.subflow.service.Page;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/** Encodes "after invoice id N" as an opaque string for API clients. */
public final class InvoiceCursor {
  private InvoiceCursor() { }

  public static String encode(long afterId) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(
        ("after:" + afterId).getBytes(StandardCharsets.UTF_8));
  }

  public static long decode(String cursor) {
    String text;
    try {
      text = new String(Base64.getUrlDecoder().decode(cursor),
          StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid cursor");
    }
    if (!text.startsWith("after:")) {
      throw new IllegalArgumentException("Invalid cursor");
    }
    return Long.parseLong(text.substring("after:".length()));
  }

  /** Builds a page from up to limit + 1 rows, sorted by id. */
  public static Page<InvoiceSummary> page(List<InvoiceSummary> rows, int limit) {
    if (rows.size() <= limit) {
      return new Page<>(rows, Optional.empty());
    }
    List<InvoiceSummary> items = rows.subList(0, limit);
    return new Page<>(items, Optional.of(encode(items.get(limit - 1).id())));
  }
}
