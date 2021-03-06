package structure;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DAFSA {
  String previousWord;
  Node root;
  List<Object[]> uncheckedNodes;
  Map<Node, Node> minimizedNodes;
  List<String> data;

  public DAFSA() {
    previousWord = "";
    root = new Node();
    uncheckedNodes = new ArrayList<>();
    minimizedNodes = new HashMap<>();
    data = new ArrayList<>();
  }

  public void insert(String word, String data) {
    if (-1 == word.compareTo(previousWord)) {
      System.out.println("Error: Words must be inserted in alphabetical order");
      return;
    }

    int commonPrefix = 0;
    int wordSize = word.length();
    int previousWordSize = previousWord.length();
    int minSize = Math.min(wordSize, previousWordSize);
    for (int i = 0; i < minSize; i++) {
      if (word.charAt(i) != previousWord.charAt(i)) {
        break;
      }
      commonPrefix++;
    }

    minimize(commonPrefix);

    this.data.add(data);

    Node node = null;
    if (uncheckedNodes.isEmpty()) {
      node = root;
    } else {
      node = (Node) uncheckedNodes.get(uncheckedNodes.size() - 1)[2];
    }

    for (int i = commonPrefix; i < word.length(); i++) {
      char letter = word.charAt(i);
      Node nextNode = new Node();
      node.edges.put(letter, nextNode);
      uncheckedNodes.add(new Object[]{node, letter, nextNode});
      node = nextNode;
    }

    node.finalNode = true;
    previousWord = word;
  }

  public void finish() {
    minimize(0);

    Node.numReachable(root);
  }

  public void minimize(int downTo) {
    for (int i = uncheckedNodes.size() - 1; downTo - 1 < i; i--) {
      Object[] o = uncheckedNodes.get(i);
      Node parent = (Node) o[0];
      char letter = (char) o[1];
      Node child = (Node) o[2];

      if (minimizedNodes.containsKey(child)) {
        parent.edges.replace(letter, minimizedNodes.get(child));
      } else {
        minimizedNodes.put(child, child);
      }
      uncheckedNodes.remove(i);
    }
  }

  public String lookup(String word) {
    Node node = root;
    int skipped = 0;
    for (int i = 0; i < word.length(); i++) {
      char letter = word.charAt(i);
      if (!node.edges.containsKey(letter)) {
        return null;
      }
      List<Character> list = new ArrayList<>(node.edges.keySet());
      Collections.sort(list);
      for (Character label : list) {
        Node child = node.edges.get(label);
        if (label == letter) {
          if (node.finalNode) {
            skipped++;
          }
          node = child;
          break;
        }
        skipped += child.wordsReachable;
      }
    }

    if (node.finalNode) {
      return data.get(skipped);
    }

    return null;
  }

  public int nodeCount() {
    return minimizedNodes.size();
  }

  public int edgeCount() {
    int count = 0;
    for (Entry<Node, Node> e : minimizedNodes.entrySet()) {
      Node node = e.getKey();
      count += node.edges.size();
    }
    return count;
  }

  public int wordToIndex(String word) {
    int index = 0;
    Node current = root;
    for (int i = 0; i < word.length(); i++) {
      char c = word.charAt(i);
      Node next = current.edges.get(c);
      if (null == next) {
        return -1;
      }
      SortedMap<Character, Node> children = current.edges.subMap(current.edges.firstKey(), c);
      for (Entry<Character, Node> child : children.entrySet()) {
        index += child.getValue().wordsReachable;
      }
      current = next;
      if (current.finalNode) {
        index++;
      }
    }
    return index;
  }

  public String indexToWord(int index) {
    String word = "";
    int count = index;
    Node current = root;
    while (0 < count) {
      SortedMap<Character, Node> children = current.edges;
      for (Entry<Character, Node> child : children.entrySet()) {
        Node tmp = child.getValue();
        if (tmp.wordsReachable < count) {
          count -= tmp.wordsReachable;
        } else {
          word += child.getKey();
          current = tmp;
          if (current.finalNode) {
            count--;
          }
          break;
        }
      }
    }
    return word;
  }

  @Override
  public String toString() {
    List<Node> nodes = new ArrayList<>();
    Stack<Node> stack = new Stack<>();
    stack.push(root);
    while (!stack.isEmpty()) {
      Node current = stack.pop();
      if (!nodes.contains(current)) {
        nodes.add(current);
      }
      stack.addAll(current.edges.values());
    }

    Collections.sort(nodes, (x, y) -> {
      return x.id - y.id;
    });

    String output = "";
    for (Node node : nodes) {
      output += node.id;
      output += node.finalNode ? "*" : "";
      output += "\n";

      for (Entry<Character, Node> child : node.edges.entrySet()) {
        output += String.format("  %s -> %d\n", child.getKey(), child.getValue().id);
      }
    }

    return output;
  }

  public String displayTree() {
    Node n = new Node();

    String tree = "";
    java.util.Queue<Node> q = new ArrayBlockingQueue<>(root.wordsReachable);
    q.add(root);
    q.add(n);

    String level = "";
    while (!(q.size() == 1 && n.equals(q.peek()))) {
      Node current = q.poll();
      if (n.equals(current)) {
        level += "  ";
        q.add(n);
        continue;
      }
      tree += level + current.id + (current.finalNode ? "*" : " ") + "\n";
      for (Entry<Character, Node> child : current.edges.entrySet()) {
        if (!q.contains(child.getValue())) {
          q.add(child.getValue());
        }
      }
    }

    return tree;
  }

  public static class Node {
    Iterator<Integer> stream =
        Stream.iterate(0, i -> i + 1).iterator();
//      new Random().ints().boxed().iterator();

    int id;
    boolean finalNode;
    SortedMap<Character, Node> edges;
    int wordsReachable;

    public Node() {
      id = stream.next();
      finalNode = false;
      edges = new TreeMap<>();
      wordsReachable = 0;
    }

    @Override
    public String toString() {
      return "BinaryTreeNode [finalNode=" + finalNode + ", edges=" + edges + "]";
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((edges == null) ? 0 : edges.hashCode());
      result = prime * result + (finalNode ? 1231 : 1237);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Node other = (Node) obj;
      if (edges == null) {
        if (other.edges != null)
          return false;
      } else if (!edges.equals(other.edges))
        return false;
      return finalNode == other.finalNode;
    }

    public static int numReachable(Node current) {
      int count = 0;
      if (current.finalNode) {
        count++;
      }
      for (Entry<Character, Node> e : current.edges.entrySet()) {
        count += numReachable(e.getValue());
      }

      current.wordsReachable = count;
      return count;
    }
  }
}
