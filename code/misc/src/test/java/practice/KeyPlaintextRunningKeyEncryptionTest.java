package practice;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class KeyPlaintextRunningKeyEncryptionTest {
	@Test
	public void test() {
		List<String> lines = Arrays.asList("SGZVQBUQAFRWSLC", "ACM");
		List<String> expected = Arrays.asList("SENDMOREMONKEYS");

		if (0 == lines.size() % 2) {
			for (int i = 0; i < lines.size(); i += 2) {
				String ciphertext = lines.get(i);
				String key = lines.get(i + 1);

				String plaintext = KeyPlaintextRunningKeyEncryption.decrypt(key, ciphertext);
				Assert.assertEquals(expected.get(i / 2), plaintext);

				String encryptPlaintext = KeyPlaintextRunningKeyEncryption.encrypt(key, plaintext);
				Assert.assertEquals(ciphertext, encryptPlaintext);
			}
		}
	}
}
