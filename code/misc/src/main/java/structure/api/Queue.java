package structure.api;

public interface Queue<T> {
  void enqueue(T value);

  boolean dequeue();
}
