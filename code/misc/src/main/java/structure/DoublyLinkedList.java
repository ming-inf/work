package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedList<S> {
	private Node<S> head;
	private Node<S> last;

	public boolean search(S value) {
		if (isNull(value)) {
			return false;
		}
		Node<S> current = head;
		while (nonNull(current) && !value.equals(current.value)) {
			current = current.next;
		}
		return null != current;
	}

	public void insert(S value) {
		if (isNull(value)) {
			return;
		}
		Node<S> newNode = new Node<>(value);

		if (isNull(head)) {
			head = last = newNode;
		} else {
			last.next = newNode;
			newNode.prev=last;
			last = newNode;
		}
	}

	public boolean delete(S value) {
		if (isNull(value) || isNull(head)) {
			return false;
		}
		Node<S> current = head;
		while (nonNull(current) && !value.equals(current.value)) {
			current = current.next;
		}

		boolean isFound = nonNull(current);
		if (isFound) {
			Node<S> prev = current.prev;
			Node<S> next = current.next;
			boolean isHeadDeleted = isNull(prev);
			boolean isLastDeleted = isNull(next);
			if (isHeadDeleted) {
				head = next;
			} else {
				prev.next = next;
				prev.prev=null;
			}

			if (isLastDeleted) {
				last = prev;
			} else {
				next.prev=prev;
				next.next = null;
			}

			current.clear();
		}
		return isFound;
	}

	public List<S> traverse() {
		List<S> list = new ArrayList<>();
		Node<S> current = head;

		while (nonNull(current)) {
			list.add(current.value);
			current = current.next;
		}

		return list;
	}

	public List<S> reverseTraverse() {
		List<S> list = new ArrayList<>();
		Node<S> current = head;
		while (nonNull(current.next)) {
			current = current.next;
		}

		while (nonNull(current)) {
			list.add(current.value);
			current = current.prev;
		}

		return list;
	}

	public String toString() {
		Node<S> current = head;
		StringBuffer sb = new StringBuffer();
		while (nonNull(current)) {
			sb.append(current.value + ", ");
			current = current.next;
		}
		return sb.toString();
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
