package examples.ch09.productsync;

import java.time.Instant;

public interface ImportFiles {
  ImportFile create(String retailerId, Instant runStartedAt);
}
