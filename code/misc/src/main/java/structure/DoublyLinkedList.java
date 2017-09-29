package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DoublyLinkedList<T> implements List<T> {
	private Node<T> head;
	private Node<T> last;
	private int size;

	public boolean contains(T value) {
		if (isNull(value)) {
			return false;
		}
		return nonNull(find(value));
	}

	public void add(T value) {
		if (isNull(value)) {
			return;
		}

		Node<T> newNode = new Node<>(value);

		if (isNull(head)) {
			head = last = newNode;
		} else {
			last.next = newNode;
			newNode.prev = last;
			last = newNode;
		}

		size++;
	}

	public boolean remove(T value) {
		if (isNull(value) || isNull(head)) {
			return false;
		}

		Node<T> current = find(value);

		if (isNull(current)) {
			return false;
		}

		Node<T> prev = current.prev;
		Node<T> next = current.next;

		boolean isHeadDeleted = isNull(prev);
		boolean isLastDeleted = isNull(next);
		if (isHeadDeleted) {
			head = next;
		} else {
			prev.next = next;
			prev.prev = null;
		}

		if (isLastDeleted) {
			last = prev;
		} else {
			next.prev = prev;
			next.next = null;
		}

		current.clear();
		size--;

		return true;
	}

	public T[] traverse(Class<T> clazz) {
		return convert(this, clazz);
	}

	public T[] reverseTraverse(Class<T> clazz) {
		DoublyLinkedList<T> result = new DoublyLinkedList<>();

		Node<T> current = last;
		while (nonNull(current)) {
			result.add(current.value);
			current = current.prev;
		}

		return convert(result, clazz);
	}

	public int size() {
		return size;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(head.value.toString());
		Node<T> current = head.next;
		while (nonNull(current)) {
			sb.append(current.value + ", ");
			current = current.next;
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private T[] convert(DoublyLinkedList<T> from, Class<T> clazz) {
		T[] newArrayInstance = (T[]) java.lang.reflect.Array.newInstance(clazz, from.size());

		int index = 0;
		Node<T> current = from.head;
		while (nonNull(current)) {
			newArrayInstance[index] = current.value;
			current = current.next;
			index++;
		}
		return newArrayInstance;
	}

	private Node<T> find(T value) {
		Node<T> result = head;
		while (nonNull(result) && !value.equals(result.value)) {
			result = result.next;
		}
		return result;
	}

	private static class Node<T> {
		final T value;
		Node<T> next;
		Node<T> prev;

		public Node(T value) {
			this(value, null, null);
		}

		public Node(T value, Node<T> prev, Node<T> next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}

		public void clear() {
			next = null;
			prev = null;
		}
	}
}
