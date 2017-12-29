package cryptography;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.EAXBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.Test;

public class BouncyCastleTest {
	@Test
	public void test() throws InvalidKeyException, IOException {
		Security.addProvider(new BouncyCastleProvider());

		/*
		 * This will generate a random key, and encrypt the data
		 */
		Key key = null;
		KeyGenerator keyGen;
		Cipher encrypt = null;

		Security.addProvider(new BouncyCastleProvider());

		try {
			// "BC" is the name of the BouncyCastle provider
			keyGen = KeyGenerator.getInstance("DES", "BC");
			keyGen.init(new SecureRandom());

			key = keyGen.generateKey();

			encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding", "BC");
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}

		encrypt.init(Cipher.ENCRYPT_MODE, key);

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		CipherOutputStream cOut = new CipherOutputStream(bOut, encrypt);

		cOut.write("plaintext".getBytes());
		cOut.close();

		// bOut now contains the cipher text
	}

	@Test
	public void testAEAD() throws IllegalStateException, InvalidCipherTextException {
		final int NONCE_LEN = 8;
		final int MAC_LEN = 8;
		final int AUTHEN_LEN = 20;

		SecureRandom srng = new SecureRandom(new byte[] { 0 });
		int DAT_LEN = srng.nextInt() >>> 22; // Note: JDK1.0 compatibility
		byte[] nonce = new byte[NONCE_LEN];
		byte[] authen = new byte[AUTHEN_LEN];
		byte[] datIn = new byte[DAT_LEN];
		byte[] key = new byte[16];
		srng.nextBytes(nonce);
		srng.nextBytes(authen);
		srng.nextBytes(datIn);
		srng.nextBytes(key);

		AESEngine engine = new AESEngine();
		KeyParameter sessKey = new KeyParameter(key);
		EAXBlockCipher eaxCipher = new EAXBlockCipher(engine);

		AEADParameters params = new AEADParameters(sessKey, MAC_LEN * 8, nonce, authen);
		eaxCipher.init(true, params);

		byte[] intrDat = new byte[eaxCipher.getOutputSize(datIn.length)];
		int outOff = eaxCipher.processBytes(datIn, 0, DAT_LEN, intrDat, 0);
		outOff += eaxCipher.doFinal(intrDat, outOff);

		eaxCipher.init(false, params);
		byte[] datOut = new byte[eaxCipher.getOutputSize(outOff)];
		int resultLen = eaxCipher.processBytes(intrDat, 0, outOff, datOut, 0);
		eaxCipher.doFinal(datOut, resultLen);

		if (!areEqual(datIn, datOut)) {
			fail("EAX roundtrip failed to match");
		}
	}

	void fail(String message) {
		throw new RuntimeException(message);
	}

	boolean areEqual(byte[] a, byte[] b) {
		return Arrays.areEqual(a, b);
	}

	boolean areEqual(byte[][] left, byte[][] right) {
		if (left == null && right == null) {
			return true;
		} else if (left == null || right == null) {
			return false;
		}

		if (left.length != right.length) {
			return false;
		}

		for (int t = 0; t < left.length; t++) {
			if (areEqual(left[t], right[t])) {
				continue;
			}
			return false;
		}

		return true;
	}
}
