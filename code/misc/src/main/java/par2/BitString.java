package par2;

import org.bouncycastle.util.Arrays;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BitString {
  public String toString(int b, Function<Integer, String> encoder) {
    return encoder.apply(b);
  }

  public String toString(byte b, Function<Integer, String> encoder) {
    return toString(b & 0xff, encoder);
  }

  public String toString(byte i) {
    return toString(i, Integer::toBinaryString);
  }

  public String toString(int i) {
    return toString(i, Integer::toBinaryString);
  }

  public List<String> toString(BigInteger i) {
    return toString(i.toByteArray());
  }

  public List<String> toString(byte[] bytes) {
    return toString(bytes, Integer::toBinaryString);
  }

  public List<String> toString(byte[] bytes, Function<Integer, String> encoding) {
    List<String> result = new ArrayList<>();
    for (byte b : bytes) {
      result.add(toString(b, encoding));
    }
    return result;
  }

  public byte[] fromString(List<String> strings, Function<String, Integer> decoder) {
    ByteBuffer bb = ByteBuffer.allocate(strings.size());
    for (String s : strings) {
      bb.put(decoder.apply(s).byteValue());
    }
    return bb.array();
  }

  public byte[] fromString(int i) {
    return ByteBuffer.allocate(4).putInt(i).array();
  }

  public int fromByteArray(byte[] bytes) {
    return ByteBuffer.wrap(bytes).getInt();
  }

  public BigInteger fromByteArrayToBigInteger(byte[] bytes) {
    return new BigInteger(1, ByteBuffer.wrap(bytes).array());
  }

  public List<String> toLittleEndianString(byte[] bytes) {
    return toString(Arrays.reverse(bytes));
  }
}
