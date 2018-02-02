package structure.api;

public interface Deque<T> {
	public void addFirst(T value);

	public boolean removeFirst();

	public void addLast(T value);

	public boolean removeLast();
}
