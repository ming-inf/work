package structure.api;

public interface Traversal<T> {
	T[] traverse(Class<T> clazz);

	T[] reverseTraverse(Class<T> clazz);
}
