package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

public class SinglyLinkedList<T> {
	private Node<T> head;
	private Node<T> last;

	public boolean search(T value) {
		if (isNull(value)) {
			return false;
		}
		return nonNull(find(value).target);
	}

	public void insert(T value) {
		if (isNull(value)) {
			return;
		}

		Node<T> newNode = new Node<>(value);

		if (isNull(head)) {
			head = last = newNode;
		} else {
			last.next = newNode;
			last = newNode;
		}
	}

	public boolean delete(T value) {
		if (isNull(value) || isNull(head)) {
			return false;
		}

		Tuple<Node<T>, Node<T>> previousCurrent = find(value);
		if (isNull(previousCurrent.target)) {
			return false;
		}

		Node<T> prev = previousCurrent.previous;
		Node<T> target = previousCurrent.target;
		Node<T> next = previousCurrent.target.next;

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

		target.clear();

		return true;
	}

	public List<T> traverse() {
		List<T> result = new ArrayList<>();

		Node<T> current = head;
		while (nonNull(current)) {
			result.add(current.value);
			current = current.next;
		}

		return result;
	}

	public List<T> reverseTraverse() {
		List<T> result = new ArrayList<>();

		Node<T> current = last;
		while (nonNull(current)) {
			result.add(current.value);
			current = find(current.value).previous;
		}

		return result;
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

	private Tuple<Node<T>, Node<T>> find(T value) {
		Tuple<Node<T>, Node<T>> result = new Tuple<>(null, head);
		while (nonNull(result.target) && !value.equals(result.target.value)) {
			result.previous = result.target;
			result.target = result.target.next;
		}
		return result;
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

	private static class Tuple<X, Y> {
		public X previous;
		public Y target;

		public Tuple(X x, Y y) {
			this.previous = x;
			this.target = y;
		}
	}
}
