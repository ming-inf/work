package structure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class MerkleTreeTest {
	private MerkleTree objectUnderTest;

	@Before
	public void setup() {
		objectUnderTest = new MerkleTree();
	}

	@Test
	public void testMerkleTree() throws IOException {
		objectUnderTest.SIZE = 1;
		InputStream is = new ByteArrayInputStream("aaaaaaaa".getBytes());
		objectUnderTest.digest(is);
		System.out.println(objectUnderTest);
	}
}
