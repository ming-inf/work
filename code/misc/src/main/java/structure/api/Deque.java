package structure.api;

public interface Deque<T> {
  void addFirst(T value);

  boolean removeFirst();

  void addLast(T value);

  boolean removeLast();
}
