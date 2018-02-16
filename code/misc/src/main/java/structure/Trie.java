package structure;

public class Trie {
  TrieNode trie = new TrieNode();

  public void insert(String word) {
    trie.insert(word);
  }
}
