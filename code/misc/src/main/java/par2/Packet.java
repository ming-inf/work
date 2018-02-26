package par2;

import java.math.BigInteger;
import java.util.Arrays;

public class Packet {
  public static final String MAGIC = "PAR2\0PKT";

  public static void main(String... args) {
    byte input[] = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}; // 64 bits, signed long = -1
    BigInteger signed = new BigInteger(input);
    BigInteger unsigned = new BigInteger(1, input);
    System.out.println("Max Long : " + Long.MAX_VALUE);
    System.out.println("Min Long : " + Long.MIN_VALUE);
    System.out.println("Signed   : " + signed);
    System.out.println("Unsigned : " + unsigned);

    System.out.println("Unsigned > Long.MAX_VALUE ? " + ((unsigned.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == 1) ? "true" : "false"));
    System.out.println("Signed == -1 ? " + (signed.compareTo(BigInteger.valueOf(-1)) == 0 ? "true" : "false"));

    System.out.println("Signed * 2 : " + (signed.multiply(BigInteger.valueOf(2))));
    System.out.println("Unsigned * 2 : " + (unsigned.multiply(BigInteger.valueOf(2))));
    byte b[] = {(byte) 0xff};
    BigInteger i = new BigInteger(b);
    b = unsigned(i);
  }

  public BigInteger toUnsigned(byte[] input) {
    return new BigInteger(1, input);
  }

  public static byte[] unsigned(BigInteger value){
    byte[] array = value.toByteArray();
    return Arrays.copyOfRange(array, 1, array.length);
  }
}
