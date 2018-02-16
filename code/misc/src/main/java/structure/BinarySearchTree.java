package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import structure.api.Tree;

public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {
  private BinarySearchTreeNode<T> root;

  public boolean contains(T value) {
    if (isNull(value)) {
      return false;
    }
    return nonNull(searchNode(null, root, new BinarySearchTreeNode<>(value)).target);
  }

  private Tuple<BinarySearchTreeNode<T>, BinarySearchTreeNode<T>> searchNode(BinarySearchTreeNode<T> parent, BinarySearchTreeNode<T> current, BinarySearchTreeNode<T> value) {
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

  public void add(T value) {
    if (isNull(value)) {
      return;
    }

    BinarySearchTreeNode<T> newNode = new BinarySearchTreeNode<>(value);
    if (isNull(root)) {
      root = newNode;
    } else {
      insertNode(root, newNode);
    }
  }

  private void insertNode(BinarySearchTreeNode<T> current, BinarySearchTreeNode<T> value) {
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

  public boolean remove(T value) {
    if (isNull(value) || isNull(root)) {
      return false;
    }

    Tuple<BinarySearchTreeNode<T>, BinarySearchTreeNode<T>> parentCurrent = searchNode(null, root, new BinarySearchTreeNode<>(value));
    BinarySearchTreeNode<T> parent = parentCurrent.parent;
    BinarySearchTreeNode<T> target = parentCurrent.target;
    if (isNull(target)) {
      return false;
    }

    BinarySearchTreeNode<T> left = target.left;
    BinarySearchTreeNode<T> right = target.right;

    boolean isParentNull = isNull(parent);
    boolean isBothNull = isNull(left) && isNull(right);
    boolean isLeftOnly = nonNull(left) && isNull(right);
    boolean isRightOnly = isNull(left) && nonNull(right);
    boolean isBothNonNull = nonNull(left) && nonNull(right);

    if (!isParentNull) {
      boolean isLeftChild = parent.left == target;
      BinarySearchTreeNode<T> newChild;
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
      BinarySearchTreeNode<T> largest = left;
      while (nonNull(largest.right)) {
        largest = largest.right;
      }
      BinarySearchTreeNode<T> largestParent = searchNode(target, target.left, largest).parent;
      if (largestParent != target) {
        largestParent.right = null;
      }
      target.value = largest.value;
    }

    return true;
  }

  @Override
  public List<T> preorder() {
    return preorder(root);
  }

  private List<T> preorder(BinarySearchTreeNode<T> current) {
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

  private List<T> postorder(BinarySearchTreeNode<T> current) {
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

  private List<T> inorder(BinarySearchTreeNode<T> current) {
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

  private List<T> breadthFirst(BinarySearchTreeNode<T> current) {
    if (isNull(current)) {
      return Collections.emptyList();
    }

    List<T> l = new ArrayList<>();

    Queue<BinarySearchTreeNode<T>> q = new ArrayBlockingQueue<>(4);
    q.add(current);
    while (!q.isEmpty()) {
      BinarySearchTreeNode<T> n = q.remove();
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

  public int heightNode(BinarySearchTreeNode<T> current) {
    if (isNull(current)) {
      return 0;
    } else {
      return 1 + Math.max(heightNode(current.left), heightNode(current.right));
    }
  }
}
