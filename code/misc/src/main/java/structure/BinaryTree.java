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
  TreeNode<T> root;

  public BinaryTree() {
  }

  public BinaryTree(TreeNode<T> root) {
    this.root = root;
  }

  public boolean contains(T value) {
    if (isNull(value)) {
      return false;
    }
    return nonNull(searchNode(null, root, new TreeNode<>(value)));
  }

  private TreeNode<T> searchNode(TreeNode<T> parent, TreeNode<T> current, TreeNode<T> value) {
    if (isNull(current)) {
      return null;
    }

    if (current.value.equals(value.value)) {
      return current;
    } else {
      TreeNode<T> result = searchNode(current, current.left, value);
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

  public void add(T value) {
    if (isNull(value)) {
      return;
    }

    TreeNode<T> newNode = new TreeNode<>(value);
    if (isNull(root)) {
      root = newNode;
    } else {
      insertNode(root, newNode);
    }
  }

  private void insertNode(TreeNode<T> tree, TreeNode<T> value) {
    Queue<TreeNode<T>> q = new LinkedList<>();
    q.add(tree);

    while (!q.isEmpty()) {
      TreeNode<T> current = q.remove();

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

    TreeNode<T> target = searchNode(null, root, new TreeNode<>(value));
    if (isNull(target)) {
      return false;
    }

    return removeNode(root, target);
  }

  private boolean removeNode(TreeNode<T> tree, TreeNode<T> value) {
    TreeNode<T> temp = null;
    Queue<TreeNode<T>> q = new LinkedList<>();
    q.add(tree);

    while (!q.isEmpty()) {
      temp = q.remove();

      if (nonNull(temp.left)) {
        q.add(temp.left);
      }
      if (nonNull(temp.right)) {
        q.add(temp.right);
      }
    }

    if (temp == tree) {
      root = null;
    }

    value.value = temp.value;
    boolean isNotRoot = null != temp.parent;
    boolean isLeftChild = isNotRoot && temp.parent.left == temp;

    if (isNotRoot) {
      if (isLeftChild) {
        temp.parent.left = null;
      } else {
        temp.parent.right = null;
      }
    }

    return true;
  }

  private static final Character MARKER = '!';

  public String serialize(Function<T, Character> toCharacter) {
    List<Optional<T>> traversal = preorderSerialize(root);
    String result = detokenize(traversal, toCharacter);
    return result;
  }

  private List<Optional<T>> preorderSerialize(TreeNode<T> current) {
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
    TreeNode<T> result = preorderDeserialize(tokens);
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

  private TreeNode<T> preorderDeserialize(List<Optional<T>> tokens) {
    if (tokens.isEmpty() || !tokens.get(0).isPresent()) {
      tokens.remove(0);
      return null;
    }

    TreeNode<T> current = new TreeNode<>(tokens.get(0).get());
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

  private List<T> preorder(TreeNode<T> current) {
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

  private List<T> postorder(TreeNode<T> current) {
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

  private List<T> inorder(TreeNode<T> current) {
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

  private List<T> breadthFirst(TreeNode<T> current) {
    if (isNull(current)) {
      return Collections.emptyList();
    }

    List<T> l = new ArrayList<>();

    Queue<TreeNode<T>> q = new ArrayBlockingQueue<>(4);
    q.add(current);
    while (!q.isEmpty()) {
      TreeNode<T> n = q.remove();
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

  public int heightNode(TreeNode<T> current) {
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
  public static List<String> toUI1(TreeNode<?> current) {
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
      TreeNode<?> child = nonNull(current.left) ? current.left : current.right;
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
  public static List<String> toUI2(TreeNode<?> current) {
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
      TreeNode<?> child = nonNull(current.left) ? current.left : current.right;
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
  public static List<String> toUI3(TreeNode<?> current, int padTo) {
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
      TreeNode<?> child = nonNull(current.left) ? current.left : current.right;
      List<String> onlyChild = toUI3(child, padTo);
      String onlyChildString = String.format(rightChildPattern, onlyChild.get(0));
      result.add(onlyChildString);
    }

    return result;
  }

  public static <T> BinaryTree<T> fromUI3(String tree) {
    if (isNull(tree) || tree.isEmpty()) {
      return null;
    }

    String[] split = tree.split("\n");
    TreeNode<T> treeNode = fromUI3(split);
    return new BinaryTree<>(treeNode);
  }

  public static <T> TreeNode<T> fromUI3(String[] tree) {
    TreeNode<T> root = null;

    for (String line : tree) {

    }

    return root;
  }
}

class TreeNode<S> {
  S value;
  TreeNode<S> parent;
  TreeNode<S> left;
  TreeNode<S> right;

  public TreeNode(S value) {
    this(value, null, null, null);
  }

  public TreeNode(S value, TreeNode<S> parent, TreeNode<S> left, TreeNode<S> right) {
    this.value = value;
    this.parent = parent;
    this.left = left;
    this.right = right;
  }

  public static int leavesSize(TreeNode<?> current) {
    if (isNull(current.left) && isNull(current.right)) {
      return 1;
    }

    return leavesSize(current.left) + leavesSize(current.right);
  }

  @Override
  public String toString() {
    return "TreeNode [value=" + value + ", parent=" + parent + ", left=" + left + ", right=" + right + "]";
  }
}
