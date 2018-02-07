package structure.api;

public interface Collection<T> {
  boolean contains(T value);

  void add(T value);

  boolean remove(T value);
}
