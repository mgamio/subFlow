package examples.ch05.abstraction.before;

/** A general-purpose rule that can be combined with other rules. */
@FunctionalInterface
public interface Specification<T> {
  boolean isSatisfiedBy(T candidate);

  default Specification<T> and(Specification<T> other) {
    return candidate -> isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
  }

  default Specification<T> not() {
    return candidate -> !isSatisfiedBy(candidate);
  }
}
