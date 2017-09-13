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
		while (nonNull(current) && !value.equals(current.getValue())) {
			current = current.getNext();
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
			last.setNext(newNode);
			last = newNode;
		}
	}

	public boolean delete(S value) {
		if (isNull(value) || isNull(head)) {
			return false;
		}
		Node<S> previous = null;
		Node<S> current = head;
		while (nonNull(current) && !value.equals(current.getValue())) {
			previous = current;
			current = current.getNext();
		}

		boolean isFound = nonNull(current);
		if (isFound) {
			Node<S> prev = previous;
			Node<S> next = current.getNext();
			boolean isHeadDeleted = isNull(prev);
			boolean isLastDeleted = isNull(next);
			if (head.equals(last)) {
				head = last = null;
			} else if (isHeadDeleted) {
				head = next;
			} else if (isLastDeleted) {
				last = prev;
				prev.setNext(null);
			} else {
				prev.setNext(next);
			}

			current.clear();
		}

		return isFound;
	}

	public List<S> traverse() {
		List<S> list = new ArrayList<>();
		Node<S> current = head;

		while (nonNull(current)) {
			list.add(current.getValue());
			current = current.getNext();
		}

		return list;
	}

	public List<S> reverseTraverse() {
		List<S> list = new ArrayList<>();
		Node<S> previous = head;
		Node<S> current = last;

		while (head != current) {
			list.add(current.getValue());

			previous = head;
			while (previous.getNext() != current) {
				previous = previous.getNext();
			}
			current = previous;
		}
		list.add(current.getValue());

		return list;
	}

	public String toString() {
		Node<S> current = head;
		StringBuffer sb = new StringBuffer();
		while (nonNull(current)) {
			sb.append(current.getValue() + ", ");
			current = current.getNext();
		}
		return sb.toString();
	}

	class Node<T> {
		private T value;
		private Node<T> next;

		public Node(T value) {
			this(value, null);
		}

		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}

		public T getValue() {
			return value;
		}

		public Node<T> setNext(Node<T> next) {
			this.next = next;
			return this;
		}

		public Node<T> getNext() {
			return next;
		}

		public void clear() {
			value = null;
			next = null;
		}
	}
}
