package structure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class DAFSATest {
	DAFSA objectUnderTest;

// @Ignore
	@Test
	public void test() {
		objectUnderTest = new DAFSA();

		String words;
// words = "dog log";
// words = "cities city pities pity";
		words = "a as i is";

		String[] splitWords = words.split(" ");
		Arrays.stream(splitWords).forEach(w -> {
			objectUnderTest.insert(w, null);
		});

		objectUnderTest.finish();

		System.out.println(objectUnderTest);

		Arrays.stream(splitWords).map(objectUnderTest::wordToIndex).forEach(System.out::println);

		IntStream.range(0, splitWords.length).mapToObj(i -> {
			return splitWords[i];
		}).forEach(System.out::println);
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
