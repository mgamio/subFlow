package examples.ch09.productsync;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RateLimitedRetailerCatalogTest {
  final List<Duration> waits = new ArrayList<>();
  final FakeRetailer retailer = new FakeRetailer().product("TS-100", "Tee", "9.90", "IMG-1");

  /** Answers 429 a given number of times, then lets the call through. */
  RetailerCatalog limitedFor(int refusals) {
    int[] left = {refusals};
    return new RetailerCatalog() {
      public RetailerProduct product(String id) {
        if (left[0]-- > 0) {
          throw new RetailerRateLimitedException(Duration.ofSeconds(30));
        }
        return retailer.product(id);
      }

      public byte[] image(String id) {
        return retailer.image(id);
      }
    };
  }

  @Test
  void waitsAsLongAsTheRetailerAsksAndTriesAgain() {
    RetailerCatalog catalog = new RateLimitedRetailerCatalog(limitedFor(2), 3, waits::add);

    assertEquals("Tee", catalog.product("TS-100").name());
    assertEquals(List.of(Duration.ofSeconds(30), Duration.ofSeconds(30)), waits);
  }

  @Test
  void givesUpAfterTheLastAttempt() {
    RetailerCatalog catalog = new RateLimitedRetailerCatalog(limitedFor(5), 3, waits::add);

    assertThrows(RetailerRateLimitedException.class, () -> catalog.product("TS-100"));
    assertEquals(2, waits.size());
  }
}
