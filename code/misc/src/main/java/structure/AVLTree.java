package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<T extends Comparable<T>> {
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

	private Tuple3<Node<T>, Node<T>, List<Node<T>>> searchNodeWithPath(Node<T> parent, Node<T> current, Node<T> value, List<Node<T>> path) {
		if (isNull(current)) {
			return new Tuple3<>(null, null, null);
		}

		if (0 == current.compareTo(value)) {
			return new Tuple3<>(parent, current, path);
		} else {
			path.add(current);
			if (-1 == value.compareTo(current)) {
				return searchNodeWithPath(current, current.left, value, path);
			} else {
				return searchNodeWithPath(current, current.right, value, path);
			}
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
		updateHeight(current);
		rebalance(current);
	}

	public int height() {
		return root.height;
	}

	private void updateHeight(Node<T> current) {
		current.height = 1 + Math.max(nonNull(current.left) ? current.left.height : 0, nonNull(current.right) ? current.right.height : 0);
	}

	private void rebalance(Node<T> current) {
		int leftHeight = nonNull(current.left) ? current.left.height : 0;
		int rightHeight = nonNull(current.right) ? current.right.height : 0;

		if (1 < leftHeight - rightHeight) {
			int leftLeftHeight = nonNull(current.left.left) ? current.left.left.height : 0;
			int leftRightHeight = nonNull(current.left.right) ? current.left.right.height : 0;
			if (0 < leftLeftHeight - leftRightHeight) {
				rightRotation(current);
			} else {
				leftThenRightRotation(current);
			}
		} else if (1 < rightHeight - leftHeight) {
			int rightLeftHeight = nonNull(current.right.left) ? current.right.left.height : 0;
			int rightRightHeight = nonNull(current.right.right) ? current.right.right.height : 0;
			if (0 < rightLeftHeight - rightRightHeight) {
				rightThenLeftRotation(current);
			} else {
				leftRotation(current);
			}
		}
	}

	private void leftRotation(Node<T> current) {
		Node<T> rightNode = current.right;
		current.right = rightNode.left;
		rightNode.left = current;
		Node<T> parent = searchNode(null, root, current).parent;
		if (isNull(parent)) {
			root = rightNode;
		} else if (current.equals(parent.left)) {
			parent.left = rightNode;
		} else {
			parent.right = rightNode;
		}
		updateHeight(current);
		updateHeight(rightNode);
	}

	private void rightRotation(Node<T> current) {
		Node<T> leftNode = current.left;
		current.left = leftNode.right;
		leftNode.right = current;
		Node<T> parent = searchNode(null, root, current).parent;
		if (isNull(parent)) {
			root = leftNode;
		} else if (current.equals(parent.left)) {
			parent.left = leftNode;
		} else {
			parent.right = leftNode;
		}
		updateHeight(current);
		updateHeight(leftNode);
	}

	private void leftThenRightRotation(Node<T> current) {
		leftRotation(current.left);
		rightRotation(current);
	}

	private void rightThenLeftRotation(Node<T> current) {
		rightRotation(current.right);
		leftRotation(current);
	}

	public boolean delete(T value) {
		if (isNull(value) || isNull(root)) {
			return false;
		}

		Tuple3<Node<T>, Node<T>, List<Node<T>>> parentCurrentPath = searchNodeWithPath(null, root, new Node<>(value), new ArrayList<>());
		Node<T> parent = parentCurrentPath.parent;
		Node<T> target = parentCurrentPath.target;
		List<Node<T>> path = parentCurrentPath.path;
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

		while (!path.isEmpty()) {
			Node<T> last = path.remove(path.size() - 1);
			updateHeight(last);
			rebalance(last);
		}

		return true;
	}

	@Override
	public String toString() {
		return "AVLTree [root=" + root + "]";
	}

	private static class Node<S extends Comparable<S>> implements Comparable<Node<S>> {
		S value;
		Node<S> left;
		Node<S> right;
		int height;

		public Node(S value) {
			this(value, null, null);
		}

		public Node(S value, Node<S> left, Node<S> right) {
			this.value = value;
			this.left = left;
			this.right = right;
			this.height = 1;
		}

		@Override
		public int compareTo(Node<S> o) {
			return value.compareTo(o.value);
		}

		@Override
		public String toString() {
			return "value=" + value + ", height=" + height + ", left=" + (nonNull(left) ? left.value : "") + ", right=" + (nonNull(right) ? right.value : "")
					+ "]\n" + (nonNull(left) ? left : "") + (nonNull(right) ? right : "");
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

	private static class Tuple3<X, Y, Z> {
		public X parent;
		public Y target;
		public Z path;

		public Tuple3(X x, Y y, Z z) {
			this.parent = x;
			this.target = y;
			this.path = z;
		}
	}
}
