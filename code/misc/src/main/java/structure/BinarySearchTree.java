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
    return nonNull(searchNode(null, root, new BinarySearchTreeNode<>(value)));
  }

  private BinarySearchTreeNode<T> searchNode(BinarySearchTreeNode<T> parent, BinarySearchTreeNode<T> current, BinarySearchTreeNode<T> value) {
    if (isNull(current)) {
      return null;
    }

    if (0 == current.compareTo(value)) {
      return current;
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
      addNode(root, newNode);
    }
  }

  private void addNode(BinarySearchTreeNode<T> current, BinarySearchTreeNode<T> value) {
    if (-1 == value.compareTo(current)) {
      if (isNull(current.left)) {
        current.left = value;
      } else {
        addNode(current.left, value);
      }
    } else {
      if (isNull(current.right)) {
        current.right = value;
      } else {
        addNode(current.right, value);
      }
    }
  }

  public boolean remove(T value) {
    if (isNull(value) || isNull(root)) {
      return false;
    }

    BinarySearchTreeNode<T> target = new BinarySearchTreeNode<>(value);
    return removeNode(root, target);
  }

  private boolean removeNode(BinarySearchTreeNode<T> current, BinarySearchTreeNode<T> value) {
    if (isNull(current)) {
      return false;
    }

    if (current.equals(value)) {
      if (isNull(current.left) && isNull(current.right)) {
        if (nonNull(current.parent)) {
          if (current.equals(current.parent.left)) {
            current.parent.left = null;
          } else {
            current.parent.right = null;
          }
          return true;
        } else {
          root = null;
          return true;
        }
      } else if (nonNull(current.left) && nonNull(current.right)) {
        BinarySearchTreeNode<T> successor = current.right;
        while (nonNull(successor) && nonNull(successor.left)) {
          successor = successor.left;
        }

        if (successor.equals(current.right)) {
          current.right = successor.right;
          if (nonNull(successor.right)) {
            successor.right.parent = current.parent;
          }
        } else {
          successor.parent.left = successor.right;
          successor.right.parent = successor.parent;
        }

        current.value = successor.value;
      } else if (nonNull(current.left)) {
        current.value = current.left.value;
        current.left = current.left.left;
        if (nonNull(current.left)) {
          current.left.parent = current;
        }
      } else {
        current.value = current.right.value;
        current.right = current.right.right;
        if (nonNull(current.right)) {
          current.right.parent = current;
        }
      }
      return true;
    } else if (-1 == current.compareTo(value)) {
      return removeNode(current.right, value);
    } else {
      return removeNode(current.left, value);
    }
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
