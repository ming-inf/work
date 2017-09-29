package structure;

public interface Collection<T> {
	boolean search(T value);

	void insert(T value);

	boolean delete(T value);
}
