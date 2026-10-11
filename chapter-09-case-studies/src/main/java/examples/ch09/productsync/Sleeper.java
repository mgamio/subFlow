package examples.ch09.productsync;

import java.time.Duration;

/** Waiting, made replaceable so tests do not wait (see Chapter 4). */
public interface Sleeper {
  void sleep(Duration duration);
}
