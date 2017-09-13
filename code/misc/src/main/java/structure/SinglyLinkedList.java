package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

public class SinglyLinkedList<S> {
	private Node<S> head;

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
		Node<S> newNode = new Node<>(value, head);
		head = newNode;
	}

	public boolean delete(S value) {
		if (isNull(value)) {
			return false;
		}
		Node<S> previous = null;
		Node<S> current = head;
		while (nonNull(current) && !value.equals(current.getValue())) {
			previous = current;
			current = current.getNext();
		}

		boolean isHeadDeleted = isNull(previous);
		boolean isRestOfListDeleted = nonNull(current);
		if (isHeadDeleted) {
			head.clear();
			head = current.getNext();
		} else if (isRestOfListDeleted) {
			previous.setNext(current.getNext());
			current.clear();
		}
		return isHeadDeleted || isRestOfListDeleted;
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
		Node<S> current = head;
		while (nonNull(current.getNext())) {
			current = current.getNext();
		}

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
			next = null;
		}
	}
}
