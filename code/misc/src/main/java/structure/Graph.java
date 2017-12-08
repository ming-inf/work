package structure;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Graph {
	GraphNode node;

	public void fromString(String s) {
		String[] sections = s.split("\n\n");
		String[] nodes = sections[0].split("\n");
		String[] edges = sections[1].split("\n");

		Map<String, GraphNode> idToNode = new HashMap<>();
		for (String node : nodes) {
			String[] id_name = node.split("\\s+");
			idToNode.put(id_name[0], new GraphNode(id_name[1]));
		}

		for (String edge : edges) {
			String[] from_to = edge.split("\\s+");
			GraphNode from = idToNode.get(from_to[0]);
			GraphNode to = idToNode.get(from_to[1]);
			from.addEdge(to);
		}

		GraphNode root = findMother(idToNode);
		node = (null != root) ? root : idToNode.get(nodes[0].split("\\s+")[0]);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();

		Map<GraphNode, String> nodeToId = new HashMap<>();
		List<GraphNode> nodes = breadthFirst(node);
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
			for (GraphNode n : node.connectedTo) {
				q.add(n);
			}
		}

		return new ArrayList<>(s);
	}

	private java.util.Set<GraphNode> depthFirst(GraphNode current) {
		if (isNull(current)) {
			return Collections.emptySet();
		}

		java.util.Set<GraphNode> s = new HashSet<>();
		s.add(current);
		for (GraphNode n : current.connectedTo) {
			s.addAll(depthFirst(n));
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

		visited = depthFirst(v);
		return visited.size() == idToNode.size() ? v : null;
	}
}

class GraphNode {
	String id;
	java.util.List<GraphNode> connectedTo = new ArrayList<>();

	GraphNode(String id) {
		this.id = id;
	}

	public void addEdge(GraphNode target) {
		connectedTo.add(target);
	}
}
