package structure;

import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedList<S> {
	private Node<S> head;

	public boolean search(S value) {
		if (null == value) {
			return false;
		}
		Node<S> current = head;
		while (null != current && !value.equals(current.getValue())) {
			current = current.getNext();
		}
		return null != current;
	}

	public void insert(S value) {
		if (null == value) {
			return;
		}
		Node<S> newNode = new Node<>(value, null, head);
		head = newNode;
		if (null != head.getNext()) {
			head.getNext().setPrev(head);
		}
	}

	public boolean delete(S value) {
		if (null == value || null == head) {
			return false;
		}
		Node<S> current = head;
		while (null != current && !value.equals(current.getValue())) {
			current = current.getNext();
		}

		boolean isFound = null != current;
		if (isFound) {
			boolean isHeadDeleted = null == current.getPrev();
			boolean isTailDeleted = null == current.getNext();
			if (isHeadDeleted) {
				head = current.getNext();
				if (null != head) {
					head.setPrev(null);
				}
			} else if (isTailDeleted) {
				Node<S> prev = current.getPrev();
				if (null != prev) {
					prev.setNext(null);
				}
			} else if (isFound) {
				Node<S> prev = current.getPrev();
				Node<S> next = current.getNext();
				current.clear();
				prev.setNext(next);
				next.setPrev(prev);
			}
		}
		return isFound;
	}

	public List<S> traverse() {
		List<S> list = new ArrayList<>();
		Node<S> current = head;

		while (null != current) {
			list.add(current.getValue());
			current = current.getNext();
		}

		return list;
	}

	public List<S> reverseTraverse() {
		List<S> list = new ArrayList<>();
		Node<S> current = head;
		while (null != current.getNext()) {
			current = current.getNext();
		}

		while (null != current) {
			list.add(current.getValue());
			current = current.getPrev();
		}

		return list;
	}

	public String toString() {
		Node<S> current = head;
		StringBuffer sb = new StringBuffer();
		while (null != current) {
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
			next = null;
			prev = null;
		}
	}
}
