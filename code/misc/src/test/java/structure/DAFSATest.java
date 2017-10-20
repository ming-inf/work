package structure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DAFSATest {
	DAFSA objectUnderTest;

//	@Ignore
	@Test
	public void test() {
		objectUnderTest = new DAFSA();

//		objectUnderTest.insert("dog", "");
//		objectUnderTest.insert("log", "");
		
		objectUnderTest.insert("city", "");
		objectUnderTest.insert("cities", "");
		objectUnderTest.insert("pity", "");
		objectUnderTest.insert("pities", "");

		objectUnderTest.finish();
		objectUnderTest.display();
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
