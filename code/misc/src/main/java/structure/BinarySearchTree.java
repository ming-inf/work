package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class BinarySearchTree<T extends Comparable<T>> {
	private Node<T> root;

	public boolean search(T value) {
		return nonNull(searchNode(null, root, new Node<>(value)).y);
	}

	private Tuple<Node<T>, Node<T>> searchNode(Node<T> parent, Node<T> current, Node<T> value) {
		if (isNull(current)) {
			return new Tuple<>(null, null);
		}

		if (0 == current.compareTo(value)) {
			return new Tuple<>(parent, current);
		} else if (-1 == value.compareTo(current)) {
			return searchNode(current, current.left, value);
		} else {
			return searchNode(current, current.right, value);
		}
	}

	public void insert(T value) {
		Node<T> newNode = new Node<>(value);
		if (isNull(root)) {
			root = newNode;
		} else {
			insertNode(root, newNode);
		}
	}

	private void insertNode(Node<T> root, Node<T> value) {
		Node<T> current = root;
		if (-1 == value.compareTo(current)) {
			if (isNull(current.left)) {
				current.left = value;
			} else {
				insertNode(current.left, value);
			}
		} else {
			if (isNull(current.right)) {
				current.right = value;
			} else {
				insertNode(current.right, value);
			}
		}
	}

	public boolean delete(T value) {
		Tuple<Node<T>, Node<T>> parentCurrent = searchNode(null, root, new Node<>(value));
		Node<T> parent = parentCurrent.x;
		Node<T> target = parentCurrent.y;
		if (isNull(target)) {
			return false;
		}

		if (isNull(target.right)) {
			if (isNull(target.left)) {
				if (nonNull(parent)) {
					if (parent.left == target) {
						parent.left = null;
					} else {
						parent.right = null;
					}
				} else {
					root = null;
				}
			} else {
				if (nonNull(parent)) {
					if (parent.left == target) {
						parent.left = target.left;
					} else {
						parent.right = target.left;
					}
				} else {
					root.value = target.left.value;
					target.left = null;
				}
			}
		} else {
			if (isNull(target.left)) {
				if (nonNull(parent)) {
					if (parent.left == target) {
						parent.left = target.right;
					} else {
						parent.right = target.right;
					}
				} else {
					root.value = target.right.value;
					target.right = null;
				}
			} else {
				Node<T> largest = target.left;
				while (nonNull(largest.right)) {
					largest = largest.right;
				}
				Node<T> largestParent = searchNode(target, target.left, largest).x;
				if (!largestParent.equals(target)) {
					largestParent.right = null;
				}
				target.value = largest.value;
			}
		}
		return true;
	}

	private static class Node<S extends Comparable<S>> implements Comparable<Node<S>> {
		S value;
		Node<S> left;
		Node<S> right;

		public Node(S value) {
			this(value, null, null);
		}

		public Node(S value, Node<S> left, Node<S> right) {
			this.value = value;
			this.left = left;
			this.right = right;
		}

		@Override
		public int compareTo(Node<S> o) {
			return value.compareTo(o.value);
		}

		@Override
		public String toString() {
			return "Node [value=" + value + ", left=" + left + ", right=" + right + "]";
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
