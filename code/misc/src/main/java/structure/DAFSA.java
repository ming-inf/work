package structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

public class DAFSA {
	String previousWord;
	Node root;

	java.util.List<Object[]> uncheckedNodes;

	Map<Node, Node> minimizedNodes;

	java.util.List<String> data;

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
			uncheckedNodes.add(new Object[] { node, letter, nextNode });
			node = nextNode;
		}

		node.finalNode = true;
		previousWord = word;
	}

	public void finish() {
		minimize(0);

		root.numReachable();
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
			java.util.List<Character> list = new ArrayList<>(node.edges.keySet());
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
				skipped += child.count;
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

	public void display() {
		Stack<Node> stack = new Stack<>();
		stack.push(root);
		java.util.Set<Integer> done = new HashSet<>();
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			if (done.contains(node.id)) {
				continue;
			}
			done.add(node.id);
			System.out.println(node.id + " " + node.finalNode);
			java.util.List<Character> list = new ArrayList<>(node.edges.keySet());
			Collections.sort(list);
			for (Character label : list) {
				Node child = node.edges.get(label);
				System.out.println(String.format("  %s goto %s", label, child.id));
				stack.push(child);
			}
		}
	}
}

class Node {
	static int nextId = 0;

	int id;
	boolean finalNode;
	Map<Character, Node> edges;
	int count;

	public Node() {
		id = nextId++;
		finalNode = false;
		edges = new HashMap<>();
		count = -1;
	}

	@Override
	public String toString() {
		return "Node [finalNode=" + finalNode + ", edges=" + edges + "]";
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
		if (finalNode != other.finalNode)
			return false;
		return true;
	}

	public int numReachable() {
		if (0 <= count) {
			return count;
		}

		int count = 0;
		if (finalNode) {
			count++;
		}
		for (Entry<Character, Node> e : edges.entrySet()) {
			count += e.getValue().numReachable();
		}

		this.count = count;
		return count;
	}
}