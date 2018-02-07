package icu4j;

import com.ibm.icu.text.Transliterator;

import java.io.IOException;
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

  public static void main(String[] args) {
    ICU4JUtil o = new ICU4JUtil();
//    String s = "漢字";
    String s = "國簡體";
    byte[] encoded = o.encode(s, Charset.forName("big5"));
    System.out.println(new String(encoded));
    String decoded = o.decode(encoded, Charset.forName("big5"));
    System.out.println(decoded);

    String decoded2 = o.decode(s.getBytes(), Charset.forName("big5"));
    System.out.println(decoded2);
    String encoded2 = new String(o.encode(decoded2, Charset.forName("big5")));
    System.out.println(encoded2);

    byte[] gb = o.transcode(encoded, Charset.forName("big5"), Charset.forName("gb18030"));
    System.out.println(new String(gb));
    System.out.println(new String(gb, Charset.forName("gb18030")));

    Transliterator t2s = Transliterator.getInstance("hant-hans");
    System.out.println("traditional to simplified:" + t2s.transliterate(s));

    Transliterator s2t = Transliterator.getInstance("hant-hans", Transliterator.REVERSE);
    System.out.println("traditional to simplified:" + s2t.transliterate(s));
  }
}
