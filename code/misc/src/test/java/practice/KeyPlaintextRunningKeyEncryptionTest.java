package practice;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class KeyPlaintextRunningKeyEncryptionTest {
	@Test
	public void test() {
		List<String> lines = Arrays.asList("SGZVQBUQAFRWSLC", "ACM");

		if (0 == lines.size() % 2) {
			for (int i = 0; i < lines.size(); i += 2) {
				String ciphertext = lines.get(i);
				String key = lines.get(i + 1);
				System.out.println(String.format("%s %s -> %s", ciphertext, key, KeyPlaintextRunningKeyEncryption.decrypt(key, ciphertext)));
			}
		}

		String plaintext = "SENDMOREMONKEYS";
		String key = "ACM";
		System.out.println(String.format("%s %s -> %s", plaintext, key, KeyPlaintextRunningKeyEncryption.encrypt(key, plaintext)));
	}
}
