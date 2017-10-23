package structure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class DAFSATest {
	DAFSA objectUnderTest;

//	@Ignore
	@Test
	public void test() {
		objectUnderTest = new DAFSA();

//		objectUnderTest.insert("dog", "");
//		objectUnderTest.insert("log", "");

//		objectUnderTest.insert("cities", "");
//		objectUnderTest.insert("city", "");
//		objectUnderTest.insert("pities", "");
//		objectUnderTest.insert("pity", "");

		objectUnderTest.insert("a", null);
		objectUnderTest.insert("as", null);
		objectUnderTest.insert("i", null);
		objectUnderTest.insert("is", null);

		objectUnderTest.finish();
		System.out.println(objectUnderTest.display());

//		System.out.println(objectUnderTest.wordToIndex("cities"));
//		System.out.println(objectUnderTest.wordToIndex("city"));
//		System.out.println(objectUnderTest.wordToIndex("pities"));
//		System.out.println(objectUnderTest.wordToIndex("pity"));

//		System.out.println(objectUnderTest.indexToWord(1));
//		System.out.println(objectUnderTest.indexToWord(2));
//		System.out.println(objectUnderTest.indexToWord(3));
//		System.out.println(objectUnderTest.indexToWord(4));
//		System.out.println(objectUnderTest.indexToWord(5));
	}

	@Test
	public void testManyWordsFromFile() {
		String filename = "words.txt";
		InputStream is = DAFSA.class.getClassLoader().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		Stream<String> words = reader.lines();

		objectUnderTest = new DAFSA();

		for (String word : words.collect(Collectors.toList())) {
			objectUnderTest.insert(word, "");
		}

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

		for (String word : words.collect(Collectors.toList())) {
			objectUnderTest.insert(word, "");
		}

		objectUnderTest.finish();

		Assert.assertNull(objectUnderTest.lookup("kwyjibo"));
	}
}
