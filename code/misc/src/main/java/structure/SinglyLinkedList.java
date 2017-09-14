package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

public class SinglyLinkedList<S> {
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
		return nonNull(current);
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
			last = newNode;
		}
	}

	public boolean delete(S value) {
		if (isNull(value) || isNull(head)) {
			return false;
		}
		Node<S> previous = null;
		Node<S> current = head;
		while (nonNull(current) && !value.equals(current.value)) {
			previous = current;
			current = current.next;
		}

		boolean isFound = nonNull(current);
		if (isFound) {
			Node<S> prev = previous;
			Node<S> next = current.next;
			boolean isHeadDeleted = isNull(prev);
			boolean isLastDeleted = isNull(next);
			if (isHeadDeleted) {
				head = next;
			} else {
				prev.next = next;
			}

			if (isLastDeleted) {
				last = prev;
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
		Node<S> previous = head;
		Node<S> current = last;

		while (head != current) {
			list.add(current.value);

			previous = head;
			while (previous.next != current) {
				previous = previous.next;
			}
			current = previous;
		}
		list.add(current.value);

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

		public Node(T value) {
			this(value, null);
		}

		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}

		public void clear() {
			next = null;
		}
	}
}
