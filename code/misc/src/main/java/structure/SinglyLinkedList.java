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
		return nonNull(find(value).y);
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

		Tuple<Node<S>, Node<S>> previousCurrent = find(value);
		if (isNull(previousCurrent.y)) {
			return false;
		}

		Node<S> prev = previousCurrent.x;
		Node<S> target = previousCurrent.y;
		Node<S> next = previousCurrent.y.next;

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

	public List<S> traverse() {
		List<S> result = new ArrayList<>();

		Node<S> current = head;
		while (nonNull(current)) {
			result.add(current.value);
			current = current.next;
		}

		return result;
	}

	public List<S> reverseTraverse() {
		List<S> result = new ArrayList<>();

		Node<S> current = last;
		while (nonNull(current)) {
			result.add(current.value);
			current = find(current.value).x;
		}

		return result;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(head.value.toString());
		Node<S> current = head.next;
		while (nonNull(current)) {
			sb.append(current.value + ", ");
			current = current.next;
		}
		return sb.toString();
	}

	private Tuple<Node<S>, Node<S>> find(S value) {
		Tuple<Node<S>, Node<S>> result = new Tuple<>(null, head);
		while (nonNull(result.y) && !value.equals(result.y.value)) {
			result.x = result.y;
			result.y = result.y.next;
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
		public X x;
		public Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}
}
