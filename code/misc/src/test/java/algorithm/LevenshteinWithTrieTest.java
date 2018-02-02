package algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class LevenshteinWithTrieTest {
	LevenshteinWithTrie objectUnderTest;

	@Test
	public void testLevenshteinWithTrie() {
		objectUnderTest = new LevenshteinWithTrie();

		String wordsString = "a an as and ant";
		List<String> words = Arrays.asList(wordsString.split(" "));
		words.forEach(objectUnderTest::insert);

		Set<String> expected = new HashSet<>(Arrays.asList(wordsString.split(" ")));
		Assert.assertEquals(expected, objectUnderTest.search("an", 1).keySet());
	}
}
