package structure;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
  String word;
  Map<Character, TrieNode> children = new HashMap<>();

  public void insert(String word) {
    TrieNode node = this;
    for (char letter : word.toCharArray()) {
      if (!children.containsKey(letter)) {
        node.children.put(letter, new TrieNode());
      }
      node = node.children.get(letter);
    }
    node.word = word;
  }
}
