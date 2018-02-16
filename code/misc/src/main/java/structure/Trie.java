package structure;

import java.util.HashMap;
import java.util.Map;

public class Trie {
  TrieNode trie = new TrieNode();

  public void insert(String word) {
    trie.insert(word);
  }
}
