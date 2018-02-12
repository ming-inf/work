package icu4j;

import com.ibm.icu.text.IDNA;
import com.ibm.icu.text.SpoofChecker;
import com.ibm.icu.text.Transliterator;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class ICU4JUtilTest {
  ICU4JUtil objectUnderTest = new ICU4JUtil();
  String traditional = "漢字國簡體";
  String simplified = "汉字国简体";

  @Test
  public void testTranscodingFromUniToBig5ToUni() {
    byte[] encoded = objectUnderTest.encode(traditional, Charset.forName("big5"));
    String decoded = objectUnderTest.decode(encoded, Charset.forName("big5"));
    Assert.assertEquals(traditional, decoded);
  }

  @Test
  public void testTranscodingIncorrectInitialEncodingAssumption() {
    String decoded = objectUnderTest.decode(traditional.getBytes(), Charset.forName("big5")); // incorrectly assume string is encoded in big5
    String encoded = new String(objectUnderTest.encode(decoded, Charset.forName("big5")));
    Assert.assertNotEquals(traditional, encoded);
  }

  @Test
  public void testTranscodingFromUniToBig5ToGBToUni() {
    byte[] encoded = objectUnderTest.encode(traditional, Charset.forName("big5"));
    byte[] gb = objectUnderTest.transcode(encoded, Charset.forName("big5"), Charset.forName("gb18030"));
    Assert.assertEquals(traditional, new String(gb, Charset.forName("gb18030")));
  }

  @Test
  public void testTranscriptingFromTraditionalToSimplified() {
    Transliterator t2s = Transliterator.getInstance("hant-hans");
    Assert.assertEquals(simplified, t2s.transliterate(traditional));
  }

  @Test
  public void testTranscriptingFromSimplifiedToTraditional() {
    Transliterator s2t = Transliterator.getInstance("hant-hans", Transliterator.REVERSE);
    Assert.assertEquals(traditional, s2t.transliterate(simplified));
  }

  @Test
  public void testIDNA() {
    String ascii = "jane_doe";
    String unicode = "jаne_doe"; // with Cyrillic 'а' characters
    IDNA idna = IDNA.getUTS46Instance(IDNA.DEFAULT);
    StringBuilder sb = new StringBuilder();
    IDNA.Info info = new IDNA.Info();
    Assert.assertNotEquals(idna.labelToASCII(ascii, sb, info).toString(), idna.labelToASCII(unicode, sb, info).toString());
  }

  @Test
  public void testConfusable() {
    SpoofChecker sc = new SpoofChecker.Builder()
        .setAllowedChars(SpoofChecker.RECOMMENDED.cloneAsThawed().addAll(SpoofChecker.INCLUSION))
        .setRestrictionLevel(SpoofChecker.RestrictionLevel.MODERATELY_RESTRICTIVE)
        .setChecks(SpoofChecker.ALL_CHECKS)
        .build();
    Assert.assertTrue(sc.failsChecks("pаypаl")); // with Cyrillic 'а' characters
    Assert.assertFalse(sc.failsChecks("jane_doe"));
    Assert.assertTrue(sc.failsChecks("jаne_doe")); // with Cyrillic 'а' characters
  }
}
