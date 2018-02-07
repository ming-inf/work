package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LevenshteinWithTrie {
  TrieNode root = new TrieNode();

  public void insert(String word) {
    root.insert(word);
  }

  public Map<String, Integer> search(String word, int maxCost) {
    Map<String, Integer> results = new HashMap<>();

    List<Integer> currentRow = IntStream.range(0, word.length() + 1).boxed().collect(Collectors.toList());

    for (char letter : root.children.keySet()) {
      searchRecursive(root.children.get(letter), letter, word, currentRow, results, maxCost);
    }

    return results;
  }

  public void searchRecursive(TrieNode node, char letter, String word, List<Integer> previousRow, Map<String, Integer> results, int maxCost) {
    int columns = word.length() + 1;
    List<Integer> currentRow = new ArrayList<>();
    currentRow.add(previousRow.get(0) + 1);

    for (int column = 1; column < columns; column++) {
      int insertCost = currentRow.get(column - 1) + 1;
      int deleteCost = previousRow.get(column) + 1;
      int replaceCost = previousRow.get(column - 1) + ((letter != word.charAt(column - 1) ? 1 : 0));
      currentRow.add(Math.min(insertCost, Math.min(deleteCost, replaceCost)));
    }

    if (currentRow.get(currentRow.size() - 1) <= maxCost && node.word != null) {
      results.put(node.word, currentRow.get(currentRow.size() - 1));
    }

    if (Collections.min(currentRow) <= maxCost) {
      for (char c : node.children.keySet()) {
        searchRecursive(node.children.get(c), c, word, currentRow, results, maxCost);
      }
    }
  }
}

class TrieNode {
  String word;
  Map<Character, TrieNode> children = new HashMap<>();

  public void insert(String word) {
    TrieNode node = this;
    for (char letter : word.toCharArray()) {
      if (!node.children.containsKey(letter)) {
        node.children.put(letter, new TrieNode());
      }
      node = node.children.get(letter);
    }
    node.word = word;
  }
}
