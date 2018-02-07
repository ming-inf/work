package structure;

import java.util.Arrays;

import org.junit.Test;

public class TrieTest {
  Trie objectUnderTest;

  @Test
  public void testTrie() {
    objectUnderTest = new Trie();

    String wordsString = "a an as and ant";
    java.util.List<String> words = Arrays.asList(wordsString.split(" "));
    words.forEach(objectUnderTest::insert);
  }
}
