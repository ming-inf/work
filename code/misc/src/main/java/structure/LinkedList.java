package structure;

public class LinkedList<S> {
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
		Node<S> newNode = new Node<>(value);
		newNode.setNext(head);
		head = newNode;
	}

	public boolean delete(S value) {
		if (null == value) {
			return false;
		}
		Node<S> previous = null;
		Node<S> current = head;
		while (null != current && !value.equals(current.getValue())) {
			previous = current;
			current = current.getNext();
		}
		if (null == previous) {
			head = current.getNext();
		} else {
			previous.setNext(current.getNext());
		}
		return null != previous;
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
	}
}
