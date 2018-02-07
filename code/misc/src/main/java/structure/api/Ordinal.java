package structure.api;

public interface Ordinal<T> {
  public void insert(int i, T value);

  public void delete(int i);

  public T get(int i);
}
