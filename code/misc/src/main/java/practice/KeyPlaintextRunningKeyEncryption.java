package practice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * East Central North America
 * Regional Contest 2016
 * 
 * Problem C
 * The Key to Cryptography
 */
public class KeyPlaintextRunningKeyEncryption {
	private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String encrypt(String key, String plaintext) {
		String runningKey = key + plaintext;

		List<Integer> plaintextInternal = toInternal(plaintext);
		List<Integer> runningKeyInternal = toInternal(runningKey);

		String ciphertext = IntStream
			.range(0, plaintext.length())
			.mapToObj(i -> {
				return (plaintextInternal.get(i) + runningKeyInternal.get(i)) % 26;
			})
			.map(KeyPlaintextRunningKeyEncryption::fromInt)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
		return ciphertext;
	}

	public static String decrypt(String key, String ciphertext) {
		List<Integer> ciphertextInternal = toInternal(ciphertext);
		List<Integer> runningKeyInternal = toInternal(key);

		String plaintext = IntStream
			.range(0, ciphertext.length())
			.mapToObj(i -> {
				int c = (26 + ciphertextInternal.get(i) - runningKeyInternal.get(i)) % 26;
				runningKeyInternal.add(c);
				return c;
			})
			.map(KeyPlaintextRunningKeyEncryption::fromInt)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
		return plaintext;
	}

	private static List<Integer> toInternal(String s) {
		return s.chars().map(KeyPlaintextRunningKeyEncryption::toInt).boxed().collect(Collectors.toList());
	}

	private static int toInt(int c) {
		return alphabet.indexOf(c);
	}

	private static int fromInt(int c) {
		return alphabet.charAt(c);
	}
}
