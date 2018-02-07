package structure.api;

public interface Queue<T> {
  public void enqueue(T value);

  public boolean dequeue();
}
