package structure;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class Graph {
  List<GraphNode> node;

  List<GraphNode> allNodes = new ArrayList<>();
  Multimap<GraphNode, GraphNode> allEdges = ArrayListMultimap.create();

  public void fromString(String s) {
    String[] sections = s.split("\n\n");
    String[] nodes = sections[0].split("\n");
    String[] edges = 1 < sections.length ? sections[1].split("\n") : new String[0];

    Map<String, GraphNode> idToNode = new HashMap<>();
    for (String node : nodes) {
      String[] id_name = node.split("\\s+");
      GraphNode n = new GraphNode(id_name[1 < id_name.length ? 1 : 0]);
      idToNode.put(id_name[0], n);
      allNodes.add(n);
      allEdges.put(n, null);
    }

    java.util.Set<GraphNode> connections = new HashSet<>();
    for (String edge : edges) {
      String[] from_to = edge.split("\\s+");
      GraphNode from = idToNode.get(from_to[0]);
      GraphNode to = idToNode.get(from_to[1]);
      connections.add(to);
      from.addEdge(to);
      allEdges.put(from, to);
    }

    GraphNode root = findMother(idToNode);
    node = (null != root) ? Arrays.asList(root) : findRoots(idToNode, connections);
  }

  public String toString() {
    StringBuilder result = new StringBuilder();

    Map<GraphNode, String> nodeToId = new HashMap<>();
    java.util.Set<GraphNode> allNodes = new HashSet<>();
    for (GraphNode n : node) {
      allNodes.addAll(breadthFirst(n));
    }
    List<GraphNode> nodes = new ArrayList<>(allNodes);
    for (int i = 0; i < nodes.size(); i++) {
      result.append(String.format("%s %s\n", i, nodes.get(i).id));
      nodeToId.put(nodes.get(i), "" + i);
    }

    result.append("\n");

    for (GraphNode node : nodes) {
      for (GraphNode connected : node.connectedTo) {
        result.append(String.format("%s %s\n", nodeToId.get(node), nodeToId.get(connected)));
      }
    }

    return result.toString();
  }

  private List<GraphNode> breadthFirst(GraphNode current) {
    if (isNull(current)) {
      return Collections.emptyList();
    }

    java.util.Set<GraphNode> s = new HashSet<>();

    Queue<GraphNode> q = new ArrayBlockingQueue<>(4);
    q.add(current);
    while (!q.isEmpty()) {
      GraphNode node = q.remove();
      s.add(node);
      q.addAll(node.connectedTo);
    }

    return new ArrayList<>(s);
  }

  private java.util.Set<GraphNode> depthFirst(GraphNode current) {
    if (isNull(current)) {
      return Collections.emptySet();
    }

    java.util.Set<GraphNode> s = new HashSet<>();

    java.util.Stack<GraphNode> stack = new Stack<>();
    stack.add(current);
    while (!stack.isEmpty()) {
      GraphNode node = stack.pop();
      if (!s.contains(node)) {
        s.add(node);
        stack.addAll(node.connectedTo);
      }
    }
    return s;
  }

  public GraphNode findMother(Map<String, GraphNode> idToNode) {
    GraphNode v = null;
    java.util.Set<GraphNode> visited = new HashSet<>();
    for (GraphNode n : idToNode.values()) {
      if (!visited.contains(n)) {
        visited.addAll(depthFirst(n));
        v = n;
      }
    }

    visited.clear();
    visited = depthFirst(v);
    return visited.size() == idToNode.size() ? v : null;
  }

  public List<GraphNode> findRoots(Map<String, GraphNode> idToNode, java.util.Set<GraphNode> connections) {
    java.util.Set<GraphNode> nodes = new HashSet<>(idToNode.values());
    nodes.removeAll(connections);
    return new ArrayList<>(nodes);
  }

  public boolean isCyclic() {
    boolean isCyclic = false;
    List<GraphNode> visited = new ArrayList<>();
    for (GraphNode r : node) {
      visited.clear();
      isCyclic = isCyclic || depthFirstCycle(r);
    }
    return isCyclic;
  }

  private boolean depthFirstCycle(GraphNode current) {
    if (isNull(current)) {
      return false;
    }

    java.util.Set<GraphNode> s = new HashSet<>();

    java.util.Stack<GraphNode> stack = new Stack<>();
    stack.add(current);
    while (!stack.isEmpty()) {
      GraphNode node = stack.pop();
      if (!s.contains(node)) {
        s.add(node);
        stack.addAll(node.connectedTo);
      } else {
        return true;
      }
    }
    return false;
  }

  public List<String> allTopologicalSort() {
    List<String> all = new ArrayList<>();

    List<GraphNode> zeroIndegrees = zeroIndegree(allEdges);
    Stack<GraphNode> sort = new Stack<>();
    for (GraphNode n : zeroIndegrees) {
      sort.push(n);
      Multimap<GraphNode, GraphNode> remaining = Multimaps.filterEntries(allEdges, entry -> !n.equals(entry.getValue()) && !n.equals(entry.getKey()));
      allTopologicalSortUtil(all, sort, remaining);
      sort.pop();
    }

    return all;
  }

  private void allTopologicalSortUtil(List<String> all, Stack<GraphNode> sort, Multimap<GraphNode, GraphNode> remaining) {
    if (0 == remaining.size()) {
      List<GraphNode> ids = new ArrayList<>(sort);
      all.add(String.join(" ", ids.stream().map(n -> n.id).collect(Collectors.toList())));
    }
    List<GraphNode> zeroIndegrees = zeroIndegree(remaining);
    for (GraphNode n : zeroIndegrees) {
      sort.push(n);
      Multimap<GraphNode, GraphNode> remain = Multimaps.filterEntries(remaining, entry -> !n.equals(entry.getValue()) && !n.equals(entry.getKey()));
      allTopologicalSortUtil(all, sort, remain);
      sort.pop();
    }
  }

  private List<GraphNode> zeroIndegree(Multimap<GraphNode, GraphNode> remaining) {
    List<GraphNode> zeroIndegreeNodes = new ArrayList<>();
    for (GraphNode from : remaining.keySet()) {
      if (!remaining.containsValue(from)) {
        zeroIndegreeNodes.add(from);
      }
    }
    return zeroIndegreeNodes;
  }

  public String topologicalSort() {
    Stack<GraphNode> sort = new Stack<>();
    for (GraphNode n : node) {
      depthFirstSort(sort, n);
    }

    List<String> ids = new ArrayList<>();
    while (!sort.isEmpty()) {
      ids.add(sort.pop().id);
    }

    String result = String.join(" ", ids);
    return result;
  }

  private void depthFirstSort(Stack<GraphNode> sort, GraphNode current) {
    if (isNull(node) || node.isEmpty()) {
      return;
    }

    for (GraphNode n : current.connectedTo) {
      depthFirstSort(sort, n);
      if (!sort.contains(n)) {
        sort.push(n);
      }
    }
    if (!sort.contains(current)) {
      sort.push(current);
    }
  }
}

class GraphNode {
  String id;
  List<GraphNode> connectedTo = new ArrayList<>();

  GraphNode(String id) {
    this.id = id;
  }

  public void addEdge(GraphNode target) {
    connectedTo.add(target);
  }

  public String toString() {
    return id;
  }
}
