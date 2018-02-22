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
import java.util.stream.Collectors;

import structure.api.Tree;

public class BinaryTree<T> implements Tree<T> {
  BinaryTreeNode<T> root;

  public BinaryTree() {
  }

  public BinaryTree(BinaryTreeNode<T> root) {
    this.root = root;
  }

  public boolean contains(T value) {
    if (isNull(value)) {
      return false;
    }
    return nonNull(searchNode(root, new BinaryTreeNode<>(value)));
  }

  private BinaryTreeNode<T> searchNode(BinaryTreeNode<T> current, BinaryTreeNode<T> value) {
    if (isNull(current)) {
      return null;
    }

    if (current.equals(value)) {
      return current;
    } else {
      BinaryTreeNode<T> result = searchNode(current.left, value);
      if (nonNull(result)) {
        return result;
      }

      result = searchNode(current.right, value);
      if (nonNull(result)) {
        return result;
      }

      return null;
    }
  }

  public void add(T value) {
    if (isNull(value)) {
      return;
    }

    BinaryTreeNode<T> newNode = new BinaryTreeNode<>(value);
    if (isNull(root)) {
      root = newNode;
    } else {
      insertNode(root, newNode);
    }
  }

  private void insertNode(BinaryTreeNode<T> tree, BinaryTreeNode<T> value) {
    Queue<BinaryTreeNode<T>> q = new LinkedList<>();
    q.add(tree);

    while (!q.isEmpty()) {
      BinaryTreeNode<T> current = q.remove();

      if (isNull(current.left)) {
        current.left = value;
        value.parent = current;
        break;
      } else {
        q.add(current.left);
      }

      if (isNull(current.right)) {
        current.right = value;
        value.parent = current;
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

    BinaryTreeNode<T> target = new BinaryTreeNode<>(value);

    return removeNode(root, target);
  }

  private boolean removeNode(BinaryTreeNode<T> tree, BinaryTreeNode<T> value) {
    BinaryTreeNode<T> temp = null;
    Queue<BinaryTreeNode<T>> q = new LinkedList<>();
    q.add(tree);

    BinaryTreeNode<T> target = null;
    while (!q.isEmpty()) {
      temp = q.remove();
      if (value.equals(temp)) {
        target = temp;
      }

      if (nonNull(temp.left)) {
        q.add(temp.left);
      }
      if (nonNull(temp.right)) {
        q.add(temp.right);
      }
    }

    if (isNull(target)) {
      return false;
    }

    BinaryTreeNode<T> replacement = temp;
    target.value = replacement.value;
    boolean isNotRoot = null != replacement.parent;
    boolean isLeftChild = isNotRoot && replacement.parent.left.equals(replacement);

    if (isNotRoot) {
      if (isLeftChild) {
        replacement.parent.left = null;
      } else {
        replacement.parent.right = null;
      }
    } else if (replacement.equals(target)) {
      root = null;
      return true;
    }

    return true;
  }

  private static final Character MARKER = '!';

  public String serialize(Function<T, Character> toCharacter) {
    List<Optional<T>> traversal = preorderSerialize(root);
    String result = detokenize(traversal, toCharacter);
    return result;
  }

  private List<Optional<T>> preorderSerialize(BinaryTreeNode<T> current) {
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
    BinaryTreeNode<T> result = preorderDeserialize(tokens);
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

  private BinaryTreeNode<T> preorderDeserialize(List<Optional<T>> tokens) {
    if (tokens.isEmpty() || !tokens.get(0).isPresent()) {
      tokens.remove(0);
      return null;
    }

    BinaryTreeNode<T> current = new BinaryTreeNode<>(tokens.get(0).get());
    tokens.remove(0);
    current.left = preorderDeserialize(tokens);
    current.right = preorderDeserialize(tokens);
    if (nonNull(current.left)) {
      current.left.parent = current;
    }

    if (nonNull(current.right)) {
      current.right.parent = current;
    }

    return current;
  }

  @Override
  public List<T> preorder() {
    return preorder(root);
  }

  private List<T> preorder(BinaryTreeNode<T> current) {
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

  private List<T> postorder(BinaryTreeNode<T> current) {
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

  private List<T> inorder(BinaryTreeNode<T> current) {
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

  private List<T> breadthFirst(BinaryTreeNode<T> current) {
    if (isNull(current)) {
      return Collections.emptyList();
    }

    List<T> l = new ArrayList<>();

    Queue<BinaryTreeNode<T>> q = new ArrayBlockingQueue<>(4);
    q.add(current);
    while (!q.isEmpty()) {
      BinaryTreeNode<T> n = q.remove();
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

  public int heightNode(BinaryTreeNode<T> current) {
    if (isNull(current)) {
      return 0;
    } else {
      return 1 + Math.max(heightNode(current.left), heightNode(current.right));
    }
  }

  public String toUI() {
    return toUI3(1);
  }

  public String toUI1() {
    return toUI1(root).stream().collect(Collectors.joining("\n"));
  }

  /*
a┬b┬d┬h
 │ │ └i
 │ └e┬j
 │   └k
 └c┬f┬l
   │ └m
   └g┬n
     └o
   */
  public static List<String> toUI1(BinaryTreeNode<?> current) {
    List<String> result = new ArrayList<>();
    if (isNull(current)) {
      return result;
    }

    if (isNull(current.left) && isNull(current.right)) {
      result.add(current.value.toString());
    } else if (nonNull(current.left) && nonNull(current.right)) {
      List<String> leftChild = toUI1(current.left);
      List<String> rightChild = toUI1(current.right);

      String root = String.format("%s┬%s", current.value.toString(), leftChild.get(0));

      List<String> leftDescendants = new ArrayList<>();
      for (int i = 1; i < leftChild.size(); i++) {
        leftDescendants.add(String.format(" │%s", leftChild.get(i)));
      }

      String child = String.format(" └%s", rightChild.get(0));

      List<String> rightDescendants = new ArrayList<>();
      for (int i = 1; i < rightChild.size(); i++) {
        rightDescendants.add(String.format("  %s", rightChild.get(i)));
      }

      result.add(root);
      result.addAll(leftDescendants);
      result.add(child);
      result.addAll(rightDescendants);
    } else {
      BinaryTreeNode<?> child = nonNull(current.left) ? current.left : current.right;
      String root = String.format("%s─%s", current.value.toString(), child.value.toString());
      result.add(root);
    }

    return result;
  }

  public String toUI2() {
    return toUI2(root).stream().collect(Collectors.joining("\n"));
  }

  /*
a┐
 ├b┐
 │ ├d┐
 │ │ ├h
 │ │ └i
 │ └e┐
 │   ├j
 │   └k
 └c┐
   ├f┐
   │ ├l
   │ └m
   └g┐
     ├n
     └o
   */
  public static List<String> toUI2(BinaryTreeNode<?> current) {
    List<String> result = new ArrayList<>();
    if (isNull(current)) {
      return result;
    }

    if (isNull(current.left) && isNull(current.right)) {
      result.add(current.value.toString());
    } else if (nonNull(current.left) && nonNull(current.right)) {
      List<String> leftChild = toUI2(current.left);
      List<String> rightChild = toUI2(current.right);

      String root = String.format("%s┐", current.value.toString());
      String left = String.format(" ├%s", leftChild.get(0));
      String child = String.format(" └%s", rightChild.get(0));

      List<String> leftDescendants = new ArrayList<>();
      for (int i = 1; i < leftChild.size(); i++) {
        leftDescendants.add(String.format(" │%s", leftChild.get(i)));
      }

      List<String> rightDescendants = new ArrayList<>();
      for (int i = 1; i < rightChild.size(); i++) {
        rightDescendants.add(String.format("  %s", rightChild.get(i)));
      }

      result.add(root);
      result.add(left);
      result.addAll(leftDescendants);
      result.add(child);
      result.addAll(rightDescendants);
    } else {
      BinaryTreeNode<?> child = nonNull(current.left) ? current.left : current.right;
      String root = String.format("%s┐\n └%s", current.value.toString(), child.value.toString());
      result.add(root);
    }

    return result;
  }

  public String toUI3(int padTo) {
    return toUI3(root, padTo).stream().collect(Collectors.joining("\n"));
  }

  /*
a
├b
│├d
││├h
││└i
│└e
│ ├j
│ └k
└c
 ├f
 │├l
 │└m
 └g
  ├n
  └o
   */
  public static List<String> toUI3(BinaryTreeNode<?> current, int padTo) {
    String rootPattern = "%" + padTo + "s";
    String leftChildPattern = "├" + rootPattern;
    String rightChildPattern = "└" + rootPattern;
    String leftDescendantPattern = "│" + rootPattern;
    String rightDescendantPattern = " " + rootPattern;

    List<String> result = new ArrayList<>();
    if (isNull(current)) {
      return result;
    }

    String root = String.format(rootPattern, current.value.toString());
    result.add(root);

    if (nonNull(current.left) && nonNull(current.right)) {
      List<String> leftChild = toUI3(current.left, padTo);
      List<String> rightChild = toUI3(current.right, padTo);

      String left = String.format(leftChildPattern, leftChild.get(0));
      String child = String.format(rightChildPattern, rightChild.get(0));

      List<String> leftDescendants = new ArrayList<>();
      for (int i = 1; i < leftChild.size(); i++) {
        leftDescendants.add(String.format(leftDescendantPattern, leftChild.get(i)));
      }

      List<String> rightDescendants = new ArrayList<>();
      for (int i = 1; i < rightChild.size(); i++) {
        rightDescendants.add(String.format(rightDescendantPattern, rightChild.get(i)));
      }

      result.add(left);
      result.addAll(leftDescendants);
      result.add(child);
      result.addAll(rightDescendants);
    } else if (nonNull(current.left) || nonNull(current.right)) {
      BinaryTreeNode<?> child = nonNull(current.left) ? current.left : current.right;
      List<String> onlyChild = toUI3(child, padTo);
      String onlyChildString = String.format(rightChildPattern, onlyChild.get(0));
      result.add(onlyChildString);
    }

    return result;
  }

  public static <T> BinaryTree<T> fromUI3(String tree, Function<String, T> mapper) {
    if (isNull(tree) || tree.isEmpty()) {
      return null;
    }

    String[] split = tree.split("\n");
    BinaryTreeNode<T> treeNode = fromUI3(split, mapper);
    return new BinaryTree<>(treeNode);
  }

  public static <T> BinaryTreeNode<T> fromUI3(String[] tree, Function<String, T> mapper) {
    BinaryTreeNode root = null;

    if (!"├└│".contains("" + tree[0].charAt(0))) {
      root = new BinaryTreeNode<>(mapper.apply(tree[0]));
    }

    int indexOfLeft = -1;
    int indexOfRight = -1;
    if (1 < tree.length) {
      for (int i = 1; i < tree.length; i++) {
        if ("├".contains("" + tree[i].charAt(0))) {
          indexOfLeft = i;
        } else if ("└".contains("" + tree[i].charAt(0))) {
          indexOfRight = i;
        }
      }
    }

    if (-1 != indexOfLeft) {
      int leftTreeSize = -1 != indexOfRight ? indexOfRight - indexOfLeft : tree.length - 2;
      String[] leftTree = new String[leftTreeSize];
      System.arraycopy(tree, 1, leftTree, 0, leftTreeSize);
      for (int i = 0; i < leftTreeSize; i++) {
        leftTree[i] = leftTree[i].substring(1);
      }
      BinaryTreeNode left = fromUI3(leftTree, mapper);
      root.left = left;
    }

    if (-1 != indexOfRight) {
      int rightTreeSize = tree.length - indexOfRight;
      String[] rightTree = new String[rightTreeSize];
      System.arraycopy(tree, indexOfRight, rightTree, 0, rightTreeSize);
      for (int i = 0; i < rightTreeSize; i++) {
        rightTree[i] = rightTree[i].substring(1);
      }
      BinaryTreeNode right = fromUI3(rightTree, mapper);
      if (-1 != indexOfLeft ^ -1 != indexOfRight) {
        root.left = right;
      } else {
        root.right = right;
      }
    }

    return root;
  }
}
