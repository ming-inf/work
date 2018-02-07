package structure.api;

public interface Ordinal<T> {
  void insert(int i, T value);

  void delete(int i);

  T get(int i);
}
