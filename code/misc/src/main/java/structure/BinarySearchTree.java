package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {
	private Node<T> root;

	public boolean search(T value) {
		if (isNull(value)) {
			return false;
		}
		return nonNull(searchNode(null, root, new Node<>(value)).target);
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
		if (isNull(value)) {
			return;
		}

		Node<T> newNode = new Node<>(value);
		if (isNull(root)) {
			root = newNode;
		} else {
			insertNode(root, newNode);
		}
	}

	private void insertNode(Node<T> current, Node<T> value) {
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
		if (isNull(value) || isNull(root)) {
			return false;
		}

		Tuple<Node<T>, Node<T>> parentCurrent = searchNode(null, root, new Node<>(value));
		Node<T> parent = parentCurrent.parent;
		Node<T> target = parentCurrent.target;
		if (isNull(target)) {
			return false;
		}

		Node<T> left = target.left;
		Node<T> right = target.right;

		boolean isParentNull = isNull(parent);
		boolean isBothNull = isNull(left) && isNull(right);
		boolean isLeftOnly = nonNull(left) && isNull(right);
		boolean isRightOnly = isNull(left) && nonNull(right);
		boolean isBothNonNull = nonNull(left) && nonNull(right);

		if (!isParentNull) {
			boolean isLeftChild = parent.left == target;
			Node<T> newChild;
			if (isBothNull) {
				newChild = null;
			} else if (isLeftOnly) {
				newChild = left;
			} else if (isRightOnly) {
				newChild = right;
			} else {
				newChild = isLeftChild ? parent.left : parent.right;
			}

			if (isLeftChild) {
				parent.left = newChild;
			} else {
				parent.right = newChild;
			}
		} else {
			if (isBothNull) {
				root = null;
			} else if (isLeftOnly) {
				root.value = left.value;
				left = null;
			} else if (isRightOnly) {
				root.value = right.value;
				right = null;
			}
		}

		if (isBothNonNull) {
			Node<T> largest = left;
			while (nonNull(largest.right)) {
				largest = largest.right;
			}
			Node<T> largestParent = searchNode(target, target.left, largest).parent;
			if (largestParent != target) {
				largestParent.right = null;
			}
			target.value = largest.value;
		}

		return true;
	}

	public List<T> traverse() {
		return preorder(root);
	}

	public List<T> preorder() {
		return preorder(root);
	}

	private List<T> preorder(Node<T> current) {
		if (isNull(current)) {
			return Collections.emptyList();
		}

		List<T> l = new ArrayList<>();
		l.add(current.value);
		l.addAll(preorder(current.left));
		l.addAll(preorder(current.right));
		return l;
	}

	public List<T> postorder() {
		return postorder(root);
	}

	private List<T> postorder(Node<T> current) {
		if (isNull(current)) {
			return Collections.emptyList();
		}

		List<T> l = new ArrayList<>();
		l.addAll(postorder(current.left));
		l.addAll(postorder(current.right));
		l.add(current.value);
		return l;
	}

	public List<T> inorder() {
		return inorder(root);
	}

	private List<T> inorder(Node<T> current) {
		if (isNull(current)) {
			return Collections.emptyList();
		}

		List<T> l = new ArrayList<>();
		l.addAll(inorder(current.left));
		l.add(current.value);
		l.addAll(inorder(current.right));
		return l;
	}

	public List<T> breadthFirst() {
		return breadthFirst(root);
	}

	private List<T> breadthFirst(Node<T> current) {
		if (isNull(current)) {
			return Collections.emptyList();
		}

		List<T> l = new ArrayList<>();

		Queue<Node<T>> q = new ArrayBlockingQueue<>(4);
		q.add(current);
		while (!q.isEmpty()) {
			Node<T> n = q.remove();
			l.add(n.value);
			if (nonNull(n.left)) q.add(n.left);
			if (nonNull(n.right)) q.add(n.right);
		}

		return l;
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
		public X parent;
		public Y target;

		public Tuple(X x, Y y) {
			this.parent = x;
			this.target = y;
		}
	}

	@Override
	public int height() {
		return heightNode(root);
	}

	public int heightNode(Node<T> current) {
		if (isNull(current)) {
			return 0;
		} else {
			return 1 + Math.max(heightNode(current.left), heightNode(current.right));
		}
	}
}
