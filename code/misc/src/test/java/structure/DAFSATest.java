package structure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class DAFSATest {
  DAFSA objectUnderTest;

  @Test
  public void test() {
    List<String> wordLists = Arrays.asList("dog log", "cities city pities pity", "a as i is");

    for (String words : wordLists) {
      objectUnderTest = new DAFSA();

      String[] splitWords = words.split(" ");
      Arrays.stream(splitWords).forEach(w -> {
        objectUnderTest.insert(w, null);
      });

      objectUnderTest.finish();

      for (String word : splitWords) {
        int actual = objectUnderTest.wordToIndex(word);
        Assert.assertTrue(0 < actual);
        Assert.assertTrue(actual <= splitWords.length);
      }
    }
  }

  @Test
  public void testManyWordsFromFile() {
    String filename = "words.txt";
    InputStream is = DAFSA.class.getClassLoader().getResourceAsStream(filename);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    Stream<String> words = reader.lines();

    objectUnderTest = new DAFSA();

    words.forEach(w -> {
      objectUnderTest.insert(w, null);
    });

    objectUnderTest.finish();

    Assert.assertNull(objectUnderTest.lookup("kwyjibo"));
  }

  @Test
  public void testManyWordsFromFile2() {
    String filename = "wordsEn.txt";
    InputStream is = DAFSA.class.getClassLoader().getResourceAsStream(filename);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    Stream<String> words = reader.lines();

    objectUnderTest = new DAFSA();

    words.forEach(w -> {
      objectUnderTest.insert(w, null);
    });

    objectUnderTest.finish();

    Assert.assertNull(objectUnderTest.lookup("kwyjibo"));
  }
}
