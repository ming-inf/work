package structure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class SuccinctTrieTest {
	SuccinctTrie objectUnderTest;
	java.util.List<String> words = Arrays.asList("apple", "orange", "alphapha", "lamp", "hello", "jello", "quiz");
	java.util.List<String> notIn = Arrays.asList(" ", "appl ", "appl", "applee", "directory", "contains", "the", "information", "needed", "to", "compute");

	@Test
	public void testEmptyTrie() {
		objectUnderTest = new SuccinctTrie();
		objectUnderTest.go(Collections.emptyList());
		for (String word : words) {
			Assert.assertFalse(objectUnderTest.lookup(word));
		}
	}

	@Test
	public void testAllWordsFound() {
		objectUnderTest = new SuccinctTrie();
		objectUnderTest.go(words);
		for (String word : words) {
			Assert.assertTrue(objectUnderTest.lookup(word));
		}
	}

	@Test
	public void testAllWordsNotFound() {
		objectUnderTest = new SuccinctTrie();
		objectUnderTest.go(words);
		for (String word : notIn) {
			Assert.assertFalse(objectUnderTest.lookup(word));
		}
	}

	@Test
	public void testManyWordsFromFile() {
		String filename = "words.txt";
		InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		Stream<String> words = reader.lines();

		objectUnderTest = new SuccinctTrie();
		objectUnderTest.go(words.collect(Collectors.toList()));
		
		Assert.assertFalse(objectUnderTest.lookup("kwyjibo"));
	}

	@Test
	public void testManyWordsFromFile2() {
		String filename = "wordsEn.txt";
		InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		Stream<String> words = reader.lines();

		objectUnderTest = new SuccinctTrie();
		objectUnderTest.go(words.collect(Collectors.toList()));
		
		Assert.assertFalse(objectUnderTest.lookup("kwyjibo"));
	}
}
