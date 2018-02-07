package structure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MerkleTreeTest {
  private MerkleTree objectUnderTest;

  @Before
  public void setup() {
    objectUnderTest = new MerkleTree();
  }

  @Test
  public void testMerkleTreeNotFullBuffer() throws IOException {
    InputStream is = new ByteArrayInputStream("aaaaaaaa".getBytes());
    objectUnderTest.digest(is);

    String expected = "TxiaFQ8BQkQuX/F9EMeo714YQt2yWYN3u5momWMXw+U=";
    String actual = new String(Base64.encode(objectUnderTest.root.value.hash));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testMerkleTree() throws IOException {
    objectUnderTest.SIZE = 1;
    InputStream is = new ByteArrayInputStream("aaaaaaaa".getBytes());
    objectUnderTest.digest(is);

    String expected = "cu8hsAM75jt0uoHrPY0aEMFkLjdxVCTk5ogd5HIyS4Y=";
    String actual = new String(Base64.encode(objectUnderTest.root.value.hash));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testMerkleTreeAA() throws IOException {
    objectUnderTest.SIZE = 1;
    InputStream is = new ByteArrayInputStream("aa".getBytes());
    objectUnderTest.digest(is);

    Digest sha256 = new SHA256Digest();
    int digestSize = sha256.getDigestSize();
    sha256.update((byte) 0);
    sha256.update("a".getBytes(), 0, 1);

    byte[] hashA = new byte[digestSize];
    sha256.doFinal(hashA, 0);

    Hash a = Hash.hash(hashA);
    Assert.assertTrue(objectUnderTest.contains(a));

    sha256.reset();
    sha256.update((byte) 1);
    sha256.update(hashA, 0, digestSize);
    sha256.update(hashA, 0, digestSize);
    sha256.doFinal(hashA, 0);

    Hash aa = Hash.hash(hashA);
    Assert.assertTrue(objectUnderTest.contains(aa));
  }

  @Test
  public void testMerkleTreeAB() throws IOException {
    objectUnderTest.SIZE = 1;
    InputStream is = new ByteArrayInputStream("ab".getBytes());
    objectUnderTest.digest(is);

    Digest sha256 = new SHA256Digest();
    int digestSize = sha256.getDigestSize();
    sha256.update((byte) 0);
    sha256.update("a".getBytes(), 0, 1);

    byte[] hashA = new byte[digestSize];
    sha256.doFinal(hashA, 0);

    Hash a = Hash.hash(hashA);
    Assert.assertTrue(objectUnderTest.contains(a));

    sha256.reset();
    sha256.update((byte) 0);
    sha256.update("b".getBytes(), 0, 1);

    byte[] hashB = new byte[digestSize];
    sha256.doFinal(hashB, 0);

    Hash b = Hash.hash(hashB);
    Assert.assertTrue(objectUnderTest.contains(b));

    sha256.reset();
    sha256.update((byte) 1);
    sha256.update(hashA, 0, digestSize);
    sha256.update(hashB, 0, digestSize);

    byte[] hashAB = new byte[digestSize];
    sha256.doFinal(hashAB, 0);

    Hash ab = Hash.hash(hashAB);
    Assert.assertTrue(objectUnderTest.contains(ab));
  }

  @Test
  public void testMerkleTreeAABB() throws IOException {
    objectUnderTest.SIZE = 1;
    InputStream is = new ByteArrayInputStream("aabb".getBytes());
    objectUnderTest.digest(is);

    // aa
    Digest sha256 = new SHA256Digest();
    int digestSize = sha256.getDigestSize();
    sha256.update((byte) 0);
    sha256.update("a".getBytes(), 0, 1);

    byte[] hashA = new byte[digestSize];
    sha256.doFinal(hashA, 0);

    Hash a = Hash.hash(hashA);
    Assert.assertTrue(objectUnderTest.contains(a));

    sha256.reset();
    sha256.update((byte) 1);
    sha256.update(hashA, 0, digestSize);
    sha256.update(hashA, 0, digestSize);
    sha256.doFinal(hashA, 0);

    Hash aa = Hash.hash(hashA);
    Assert.assertTrue(objectUnderTest.contains(aa));

    // bb
    sha256.reset();
    sha256.update((byte) 0);
    sha256.update("b".getBytes(), 0, 1);

    byte[] hashB = new byte[digestSize];
    sha256.doFinal(hashB, 0);

    Hash b = Hash.hash(hashB);
    Assert.assertTrue(objectUnderTest.contains(b));

    sha256.reset();
    sha256.update((byte) 1);
    sha256.update(hashB, 0, digestSize);
    sha256.update(hashB, 0, digestSize);
    sha256.doFinal(hashB, 0);

    Hash bb = Hash.hash(hashB);
    Assert.assertTrue(objectUnderTest.contains(bb));

    // aabb
    sha256.reset();
    sha256.update((byte) 1);
    sha256.update(hashA, 0, digestSize);
    sha256.update(hashB, 0, digestSize);

    byte[] hashAABB = new byte[digestSize];
    sha256.doFinal(hashAABB, 0);

    Hash aabb = Hash.hash(hashAABB);
    Assert.assertTrue(objectUnderTest.contains(aabb));
  }
}
