package algorithm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import structure.DAFSA;

public class LevenshteinTest {
	Levenshtein objectUnderTest;

	@Test
	public void testLevenshteinDistance() {
		objectUnderTest = new Levenshtein();

		String filename = "words.txt";
		InputStream is = DAFSA.class.getClassLoader().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		java.util.List<String> words = reader.lines().collect(Collectors.toList());

		Set<String> expected = new HashSet<>(Arrays.asList("obtect", "abject", "objects", "objet", "object"));
		Assert.assertEquals(expected, objectUnderTest.search("object", 1, words).keySet());
	}
}
