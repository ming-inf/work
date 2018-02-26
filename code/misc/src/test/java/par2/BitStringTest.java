package par2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;

public class BitStringTest {
  private BitString objectUnderTest;

  @Before
  public void setup() {
    objectUnderTest = new BitString();
  }

  @Test
  public void testByteToBinaryByteString() {
    Map<Byte, String> byteStringMap = new HashMap<>();
    byteStringMap.put((byte) 0, "0");
    byteStringMap.put((byte) 0xff, "11111111");

    for (Map.Entry<Byte, String> entry : byteStringMap.entrySet()) {
      Assert.assertEquals(entry.getValue(), objectUnderTest.toString(entry.getKey()));
    }
  }

  @Test
  public void testIntToBinaryString() {
    Map<Integer, String> byteStringMap = new HashMap<>();
    byteStringMap.put(0, "0");
    byteStringMap.put(0xffffffff, "11111111111111111111111111111111");

    for (Map.Entry<Integer, String> entry : byteStringMap.entrySet()) {
      Assert.assertEquals(entry.getValue(), objectUnderTest.toString(entry.getKey()));
    }
  }

  @Test
  public void testBigIntToBinaryString() {
    Map<BigInteger, List<String>> byteStringMap = new HashMap<>();
    byteStringMap.put(BigInteger.ZERO, Collections.singletonList("0"));
    byteStringMap.put(BigInteger.TEN, Collections.singletonList("1010"));
    byteStringMap.put(BigInteger.valueOf(0x0d0c0b0a), Arrays.asList("1101", "1100", "1011", "1010"));

    for (Map.Entry<BigInteger, List<String>> entry : byteStringMap.entrySet()) {
      Assert.assertEquals(entry.getValue(), objectUnderTest.toString(entry.getKey()));
    }
  }

  @Test
  public void testByteArrayToBinaryStrings() {
    Map<byte[], List<String>> byteStringMap = new HashMap<>();
    byteStringMap.put(new byte[]{(byte) 0}, Collections.singletonList("0"));
    byteStringMap.put(new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, Arrays.asList("11111111", "11111111", "11111111", "11111111"));
    byteStringMap.put(new byte[]{(byte) 0x01, (byte) 0x02}, Arrays.asList("1", "10"));

    for (Map.Entry<byte[], List<String>> entry : byteStringMap.entrySet()) {
      Assert.assertEquals(entry.getValue(), objectUnderTest.toString(entry.getKey()));
    }
  }

  @Test
  public void testFromString() {
    Map<List<String>, byte[]> byteStringMap = new HashMap<>();
    byteStringMap.put(Collections.singletonList("0"), new byte[]{(byte) 0});
    byteStringMap.put(Arrays.asList("11111111", "11111111", "11111111", "11111111"), new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff});
    byteStringMap.put(Arrays.asList("1", "10"), new byte[]{(byte) 0x01, (byte) 0x02});

    for (Map.Entry<List<String>, byte[]> entry : byteStringMap.entrySet()) {
      Assert.assertArrayEquals(entry.getValue(), objectUnderTest.fromString(entry.getKey(), s -> Integer.parseUnsignedInt(s, 2)));
    }
  }

  @Test
  public void testIntToByteArray() {
    Map<Integer, byte[]> byteStringMap = new HashMap<>();
    byteStringMap.put(0, new byte[]{0, 0, 0, 0});
    byteStringMap.put(0xff, new byte[]{0, 0, 0, (byte) 0xff});

    for (Map.Entry<Integer, byte[]> entry : byteStringMap.entrySet()) {
      Assert.assertArrayEquals(entry.getValue(), objectUnderTest.fromString(entry.getKey()));
    }
  }

  @Test
  public void testByteArrayToInteger() {
    Map<byte[], Integer> byteStringMap = new HashMap<>();
    byteStringMap.put(new byte[]{0, 0, 0, 0}, 0);
    byteStringMap.put(new byte[]{0, 0, 0, (byte) 0xff}, 0xff);

    for (Map.Entry<byte[], Integer> entry : byteStringMap.entrySet()) {
      Assert.assertEquals(0, Integer.compareUnsigned(entry.getValue(), objectUnderTest.fromByteArray(entry.getKey())));
    }
  }

  @Test
  public void testByteArrayToBigInteger() {
    Map<byte[], BigInteger> byteStringMap = new HashMap<>();
    byteStringMap.put(new byte[]{0}, BigInteger.ZERO);
    byteStringMap.put(new byte[]{(byte) 0xff}, BigInteger.valueOf(0xff));

    for (Map.Entry<byte[], BigInteger> entry : byteStringMap.entrySet()) {
      Assert.assertEquals(0, entry.getValue().compareTo(objectUnderTest.fromByteArrayToBigInteger(entry.getKey())));
    }
  }

  @Test
  public void testLittleEndian() {
    Map<byte[], List<String>> byteStringMap = new HashMap<>();
    byteStringMap.put(new byte[]{0}, Collections.singletonList("0"));
    byteStringMap.put(new byte[]{(byte) 0xff}, Collections.singletonList("11111111"));
    byteStringMap.put(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04}, Arrays.asList("100", "11", "10", "1"));
    byteStringMap.put(new byte[]{(byte) 0x0d, (byte) 0x0c, (byte) 0x0b, (byte) 0x0a}, Arrays.asList("1010", "1011", "1100", "1101"));

    for (Map.Entry<byte[], List<String>> entry : byteStringMap.entrySet()) {
      Assert.assertEquals(entry.getValue(), objectUnderTest.toLittleEndianString(entry.getKey()));
    }
  }
}
