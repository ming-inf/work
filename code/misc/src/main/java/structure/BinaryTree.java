package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Function;

import structure.api.Tree;

public class BinaryTree<T> implements Tree<T> {
	Node<T> root;

	public BinaryTree() {
	}

	public BinaryTree(Node<T> root) {
		this.root = root;
	}

	public boolean contains(T value) {
		if (isNull(value)) {
			return false;
		}
		return nonNull(searchNode(null, root, new Node<>(value)).target);
	}

	private Tuple<Node<T>, Node<T>> searchNode(Node<T> parent, Node<T> current, Node<T> value) {
		if (isNull(current)) {
			return new Tuple<>(null, null);
		}

		if (current.value.equals(value.value)) {
			return new Tuple<>(parent, current);
		} else {
			Tuple<Node<T>, Node<T>> result = searchNode(current, current.left, value);
			if (nonNull(result.target)) {
				return result;
			}

			result = searchNode(current, current.right, value);
			if (nonNull(result.target)) {
				return result;
			}

			return new Tuple<>(null, null);
		}
	}

	public void add(T value) {
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

	private void insertNode(Node<T> tree, Node<T> value) {
		Queue<Node<T>> q = new LinkedList<Node<T>>();
		q.add(tree);

		while (!q.isEmpty()) {
			Node<T> current = q.remove();

			if (isNull(current.left)) {
				current.left = value;
				break;
			} else {
				q.add(current.left);
			}

			if (isNull(current.right)) {
				current.right = value;
				break;
			} else {
				q.add(current.right);
			}
		}
	}

	public boolean remove(T value) {
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

	private static final Character MARKER = '!';

	public String serialize(Function<T, Character> toCharacter) {
		List<Optional<T>> traversal = preorderSerialize(root);
		String result = detokenize(traversal, toCharacter);
		return result;
	}

	private List<Optional<T>> preorderSerialize(Node<T> current) {
		if (isNull(current)) {
			return Collections.emptyList();
		}

		List<Optional<T>> l = new ArrayList<>();
		l.add(Optional.of(current.value));
		l.addAll(nonNull(current.left) ? preorderSerialize(current.left) : Arrays.asList(Optional.empty()));
		l.addAll(nonNull(current.right) ? preorderSerialize(current.right) : Arrays.asList(Optional.empty()));

		return l;
	}

	private String detokenize(List<Optional<T>> tokens, Function<T, Character> toCharacter) {
		String result = "";
		for (Optional<T> t : tokens) {
			result += t.isPresent() ? t.get() : MARKER;
		}
		return result;
	}

	public void deserialize(String serial, Function<Character, T> fromCharacter) {
		List<Optional<T>> tokens = tokenize(serial, fromCharacter);
		Node<T> result = preorderDeserialize(tokens);
		root = result;
	}

	private List<Optional<T>> tokenize(String serial, Function<Character, T> fromCharacter) {
		List<Optional<T>> result = new ArrayList<>();
		for (char c : serial.toCharArray()) {
			if (MARKER.equals(c)) {
				result.add(Optional.empty());
			} else {
				result.add(Optional.of(fromCharacter.apply(c)));
			}
		}
		return result;
	}

	private Node<T> preorderDeserialize(List<Optional<T>> tokens) {
		if (tokens.isEmpty() || !tokens.get(0).isPresent()) {
			tokens.remove(0);
			return null;
		}

		Node<T> current = new Node<T>(tokens.get(0).get());
		tokens.remove(0);
		current.left = preorderDeserialize(tokens);
		current.right = preorderDeserialize(tokens);

		return current;
	}

	@Override
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

	@Override
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

	@Override
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

	@Override
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
			if (nonNull(n.left)) {
				q.add(n.left);
			}

			if (nonNull(n.right)) {
				q.add(n.right);
			}
		}

		return l;
	}

	public static class Node<S> {
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
