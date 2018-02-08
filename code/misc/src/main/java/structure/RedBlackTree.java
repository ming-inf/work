package structure;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import structure.api.Tree;

public class RedBlackTree<T extends Comparable<T>> implements Tree<T> {
  private Node<T> root;

  @Override
  public boolean contains(T value) {
    if (isNull(value)) {
      return false;
    }
    return nonNull(searchNode(null, root, new Node<>(value)));
  }

  private Node<T> searchNode(Node<T> parent, Node<T> current, Node<T> value) {
    if (isNull(current)) {
      return null;
    }

    if (current.value.equals(value.value)) {
      return current;
    } else {
      Node<T> result = searchNode(current, current.left, value);
      if (nonNull(result)) {
        return result;
      }

      result = searchNode(current, current.right, value);
      if (nonNull(result)) {
        return result;
      }

      return null;
    }
  }

  @Override
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

    rebalance(newNode);
  }

  private void insertNode(Node<T> current, Node<T> value) {
    if (-1 == value.compareTo(current)) {
      if (isNull(current.left)) {
        current.left = value;
        value.parent = current;
      } else {
        insertNode(current.left, value);
      }
    } else {
      if (isNull(current.right)) {
        current.right = value;
        value.parent = current;
      } else {
        insertNode(current.right, value);
      }
    }
  }

  private void rebalance(Node<T> current) {
    if (isNull(current.parent)) {
      case1(current);
    } else if (isNull(current.parent) || current.parent.isBlack) {
      case2(current);
    } else if (nonNull(uncle(current)) && !uncle(current).isBlack) {
      case3(current);
    } else {
      case4(current);
    }
  }

  private void case1(Node<T> current) {
    current.isBlack = true;
  }

  private void case2(Node<T> current) {
  }

  private void case3(Node<T> current) {
    current.parent.isBlack = true;
    if (nonNull(uncle(current))) {
      uncle(current).isBlack = true;
    }
    if (nonNull(grandparent(current))) {
      grandparent(current).isBlack = false;
    }
    rebalance(grandparent(current));
  }

  private void case4(Node<T> current) {
    Node<T> parent = current.parent;
    Node<T> grandparent = grandparent(current);

    if (nonNull(grandparent) && nonNull(grandparent.left) && current.equals(grandparent.left.right)) {
      leftRotation(parent);
      if (nonNull(parent.left)) {
        if (parent.left.equals(current)) {
          parent.left = current.left;
        } else {
          parent.right = current.left;
        }
      }
    } else if (nonNull(grandparent) && nonNull(grandparent.right) && current.equals(grandparent.right.left)) {
      rightRotation(parent);
      if (nonNull(parent.left)) {
        if (parent.left.equals(current)) {
          parent.left = current.right;
        } else {
          parent.right = current.right;
        }
      }
    }

    case4step2(current);
  }

  private void case4step2(Node<T> current) {
    Node<T> parent = current.parent;
    Node<T> grandparent = grandparent(current);

    if (nonNull(parent.left) && current.equals(parent.left)) {
      rightRotation(grandparent);
    } else {
      leftRotation(grandparent);
    }

    if (nonNull(parent)) {
      parent.isBlack = true;
    }
    if (nonNull(grandparent)) {
      grandparent.isBlack = false;
    }
  }

  private void leftRotation(Node<T> current) {
    Node<T> rightNode = current.right;
    current.right = rightNode.left;
    if (nonNull(rightNode.left)) {
      rightNode.left.parent = current;
    }
    rightNode.parent = current.parent;
    Node<T> parent = current.parent;
    if (isNull(parent)) {
      root = rightNode;
    } else if (current.equals(parent.left)) {
      parent.left = rightNode;
    } else {
      parent.right = rightNode;
    }
    rightNode.left = current;
    current.parent = rightNode;
    updateHeight(current);
    updateHeight(rightNode);
  }

  private void rightRotation(Node<T> current) {
    Node<T> leftNode = current.left;
    current.left = leftNode.right;
    if (nonNull(leftNode.right)) {
      leftNode.right.parent = current;
    }
    leftNode.parent = current.parent;
    Node<T> parent = current.parent;
    if (isNull(parent)) {
      root = leftNode;
    } else if (current.equals(parent.left)) {
      parent.left = leftNode;
    } else {
      parent.right = leftNode;
    }
    leftNode.right = current;
    current.parent = leftNode;
    updateHeight(current);
    updateHeight(leftNode);
  }

  private void updateHeight(Node<T> current) {
    current.height = 1 + Math.max(nonNull(current.left) ? current.left.height : 0,
        nonNull(current.right) ? current.right.height : 0);
  }

  public Node<T> grandparent(Node<T> current) {
    if (isNull(current) || isNull(current.parent)) {
      return null;
    } else {
      return current.parent.parent;
    }
  }

  public Node<T> sibling(Node<T> current) {
    if (isNull(current) || isNull(current.parent)) {
      return null;
    }

    return current.equals(current.parent.left) ? current.parent.right : current.parent.left;
  }

  public Node<T> uncle(Node<T> current) {
    if (isNull(current) || isNull(current.parent)) {
      return null;
    } else {
      return sibling(current.parent);
    }
  }

  @Override
  public boolean remove(T value) {
    if (isNull(value) || isNull(root)) {
      return false;
    }

    Node<T> target = searchNode(null, root, new Node<>(value));
    if (isNull(target)) {
      return false;
    }

    if (nonNull(target.left) && nonNull(target.right)) {
      Node<T> replacement = target.right;
      while (nonNull(replacement.left)) {
        replacement = replacement.left;
      }

      target.value = replacement.value;
      target = replacement;
    }

    return removeOneChild(target);
  }

  private boolean removeOneChild(Node<T> current) {
    Node<T> child = current.right;
    Node<T> parent = current.parent;

    if (isNull(child)) {
      return true;
    }

    if (nonNull(parent) && current.equals(parent.left)) {
      parent.left = child;
    } else {
      parent.right = child;
    }
    child.parent = parent;

    if (current.isBlack) {
      if (nonNull(child) && !child.isBlack) {
        child.isBlack = true;
      } else {
        return deleteCase1(child);
      }
    }
    return true;
  }

  private boolean deleteCase1(Node<T> current) {
    if (nonNull(current.parent)) {
      return deleteCase2(current);
    }
    return true;
  }

  private boolean deleteCase2(Node<T> current) {
    Node<T> s = sibling(current);

    if (nonNull(s) && !s.isBlack) {
      current.parent.isBlack = false;
      s.isBlack = true;
      if (current.equals(current.parent.left)) {
        leftRotation(current.parent);
      } else {
        rightRotation(current.parent);
      }
    }
    return deleteCase3(current);
  }

  private boolean deleteCase3(Node<T> current) {
    Node<T> s = sibling(current);

    if (current.parent.isBlack && s.isBlack && s.left.isBlack && s.right.isBlack) {
      s.isBlack = false;
      return deleteCase1(current.parent);
    } else {
      return deleteCase4(current);
    }
  }

  private boolean deleteCase4(Node<T> current) {
    Node<T> s = sibling(current);

    if (!current.parent.isBlack && s.isBlack && s.left.isBlack && s.right.isBlack) {
      s.isBlack = false;
      current.parent.isBlack = true;
      return true;
    } else {
      return deleteCase5(current);
    }
  }

  private boolean deleteCase5(Node<T> current) {
    Node<T> s = sibling(current);

    if (s.isBlack) {
      if (current.equals(current.parent.left) && s.right.isBlack && !s.left.isBlack) {
        s.isBlack = false;
        s.left.isBlack = true;
        rightRotation(s);
      } else if (current.equals(current.parent.right) && s.left.isBlack && !s.right.isBlack) {
        s.isBlack = false;
        s.right.isBlack = true;
        leftRotation(s);
      }
    }
    return deleteCase6(current);
  }

  private boolean deleteCase6(Node<T> current) {
    Node<T> s = sibling(current);

    s.isBlack = current.parent.isBlack;
    current.parent.isBlack = true;

    if (current.equals(current.parent.left)) {
      s.right.isBlack = true;
      leftRotation(current.parent);
    } else {
      s.left.isBlack = true;
      rightRotation(current.parent);
    }
    return true;
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

  private static class Node<S extends Comparable<S>> implements Comparable<Node<S>> {
    S value;
    boolean isBlack;
    Node<S> parent;
    Node<S> left;
    Node<S> right;
    int height;

    public Node(S value) {
      this(value, false, null, null, null);
    }

    public Node(S value, boolean isBlack, Node<S> parent, Node<S> left, Node<S> right) {
      this.value = value;
      this.isBlack = isBlack;
      this.parent = parent;
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
      return "Node [value=" + value + ", isBlack=" + isBlack + ", parent=" + parent + ", left=" + left
          + ", right=" + right + ", height=" + height + "]";
    }
  }
}
