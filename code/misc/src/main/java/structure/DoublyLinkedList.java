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
		while (nonNull(current) && !value.equals(current.getValue())) {
			current = current.getNext();
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
			last.setNext(newNode);
			newNode.setPrev(last);
			last = newNode;
		}
	}

	public boolean delete(S value) {
		if (isNull(value) || isNull(head)) {
			return false;
		}
		Node<S> current = head;
		while (nonNull(current) && !value.equals(current.getValue())) {
			current = current.getNext();
		}

		boolean isFound = nonNull(current);
		if (isFound) {
			Node<S> prev = current.getPrev();
			Node<S> next = current.getNext();
			boolean isHeadDeleted = isNull(prev);
			boolean isLastDeleted = isNull(next);
			if (isHeadDeleted) {
				head = next;
			} else {
				prev.setNext(next);
				prev.setPrev(null);
			}

			if (isLastDeleted) {
				last = prev;
			} else {
				next.setPrev(prev);
				next.setNext(null);
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
		Node<S> current = head;
		while (nonNull(current.getNext())) {
			current = current.getNext();
		}

		while (nonNull(current)) {
			list.add(current.getValue());
			current = current.getPrev();
		}

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
		private Node<T> prev;

		public Node(T value) {
			this(value, null, null);
		}

		public Node(T value, Node<T> prev, Node<T> next) {
			this.value = value;
			this.prev = prev;
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

		public Node<T> setPrev(Node<T> prev) {
			this.prev = prev;
			return this;
		}

		public Node<T> getPrev() {
			return prev;
		}

		public void clear() {
			value = null;
			next = null;
			prev = null;
		}
	}
}
