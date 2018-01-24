package structure;

public interface Queue<T> {
	public void enqueue(T value);

	public boolean dequeue();
}
