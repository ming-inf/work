package structure.api;

public interface Set<T> {
  Set<T> union(Set<T> that);

  Set<T> intersection(Set<T> that);

  Set<T> compliment(Set<T> that);
}
