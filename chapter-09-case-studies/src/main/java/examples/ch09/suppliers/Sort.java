package examples.ch09.suppliers;

import java.util.Locale;

/** A sort order the API accepts, such as "name" or "name,desc". */
public record Sort<F extends Enum<F>>(F field, Direction direction) {

  public enum Direction { ASC, DESC }

  public static <F extends Enum<F>> Sort<F> parse(String text, Class<F> fields) {
    String[] parts = text.trim().split(",");
    if (parts.length > 2) {
      throw new InvalidQueryException("Invalid sort: " + text);
    }
    F field = parseEnum(parts[0], fields, "Cannot sort by '" + parts[0] + "'");
    Direction direction = parts.length == 1 ? Direction.ASC
        : parseEnum(parts[1], Direction.class, "Invalid sort direction: " + parts[1]);
    return new Sort<>(field, direction);
  }

  private static <E extends Enum<E>> E parseEnum(String text, Class<E> type, String error) {
    try {
      return Enum.valueOf(type, text.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      throw new InvalidQueryException(error);
    }
  }
}
