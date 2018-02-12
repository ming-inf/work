package icu4j;

import java.nio.charset.Charset;

public class ICU4JUtil {
  public String decode(byte[] data, Charset cs) {
    return new String(data, cs);
  }

  public byte[] encode(String data, Charset cs) {
    return data.getBytes(cs);
  }

  public byte[] transcode(byte[] data, Charset from, Charset to) {
    String decoded = decode(data, from);
    byte[] encoded = encode(decoded, to);
    return encoded;
  }
}
