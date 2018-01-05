package cryptography;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.modes.EAXBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Assert;
import org.junit.Test;

public class BouncyCastleTest {
	@Test
	public void testScryptEncrypt() throws NoSuchAlgorithmException, IOException {
		SecureRandom srng = new SecureRandom(new byte[] { 0 });
		byte[] S = new byte[32];
		srng.nextBytes(S);

		byte[] P = "password".getBytes();
		int logN = 16;
		int N = 1 << logN;
		int r = 8;
		int p = 1;
		int dkLen = 256;
		byte[] result = SCrypt.generate(P, S, N, r, p, dkLen);
		/**
		 *
 bytes 0-5: The word "scrypt"
 byte 6: 0
 byte 7: logN
 bytes 8-11: r
 bytes 12-15: p
 bytes 16-47: salt (which is 32 bytes)
 bytes 48-80: A 32 byte SHA256 checksum (hash) of the contents of bytes 0 to 47
 bytes 81-113: A 32 byte HMAC hash of bytes 0 to 80 with the key being the scrypt cryptographic hash
		 */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("scrypt".getBytes());
		baos.write(0);
		baos.write(logN);
		baos.write(ByteBuffer.allocate(4).putInt(r).array());
		baos.write(ByteBuffer.allocate(4).putInt(p).array());
		baos.write(S);

		byte[] bytes = baos.toByteArray();

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bytes);
		byte[] digest = md.digest();

		baos.write(digest);

		bytes = baos.toByteArray();

		HMac hmac = new HMac(new SHA256Digest());
		hmac.init(new KeyParameter(result));
		hmac.update(bytes, 0, bytes.length);

		byte[] scrypt = new byte[32];
		hmac.doFinal(scrypt, 0);

		baos.write(scrypt);
		System.out.println(Base64.toBase64String(baos.toByteArray()));
	}

	@Test
	public void testScryptVerify() throws NoSuchAlgorithmException {
		byte[] P = "password".getBytes();
		String ciphertext = "c2NyeXB0ABAAAAAIAAAAAXq43IRWwl8TJVHxV8d6GIjvkY+szO7URnUVfM4qkZ+SK5RvP6gl2OTAQM72q5u7omvX6afamageSaAvsP0qJnW7ypEzkfGI0djQLlte9xZakCt16RgeFq2F8t2NV+LaHg==";
		byte[] bytes = Base64.decode(ciphertext);

		int logN = bytes[7];
		int N = 1 << logN;
		int r = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 8, 12)).getInt();
		int p = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 12, 16)).getInt();
		byte[] S = Arrays.copyOfRange(bytes, 16, 48);
		int dkLen = 256;

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(Arrays.copyOfRange(bytes, 0, 48));
		byte[] digest = md.digest();

		Assert.assertTrue(Arrays.areEqual(Arrays.copyOfRange(bytes, 48, 80), digest));

		byte[] result = SCrypt.generate(P, S, N, r, p, dkLen);

		HMac hmac = new HMac(new SHA256Digest());
		hmac.init(new KeyParameter(result));
		hmac.update(bytes, 0, 80);

		byte[] scrypt = new byte[32];
		hmac.doFinal(scrypt, 0);

		Assert.assertTrue(Arrays.areEqual(Arrays.copyOfRange(bytes, 80, bytes.length), scrypt));
	}

	@Test
	public void testSymmetricEncrypt() throws InvalidKeyException, IOException {
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

		Assert.assertTrue(Arrays.areEqual(datIn, datOut));
	}
}
