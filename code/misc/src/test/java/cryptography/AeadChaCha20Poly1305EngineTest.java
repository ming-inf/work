package cryptography;

import java.io.IOException;

import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class AeadChaCha20Poly1305EngineTest {
	@Test
	public void testAEADChaChaPoly() throws IOException {
		byte[] plaintext = Hex.decode(
				"4c616469657320616e642047656e746c656d656e206f662074686520636c617373206f66202739393a204966204920636f756c64206f6666657220796f75206f6e6c79206f6e652074697020666f7220746865206675747572652c2073756e73637265656e20776f756c642062652069742e");
		byte[] aad = Hex.decode("50515253c0c1c2c3c4c5c6c7");
		byte[] key = Hex.decode("808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9f");
		byte[] nonce = Hex.decode("07000000" + "4041424344454647"); // constant + iv
//		byte[] otk = Hex.decode("7bac2b252db447af09b67a55a4e955840ae1d6731075d9eb2a9375783ed553ff");
		byte[] ciphertext = Hex.decode(
				"d31a8d34648e60db7b86afbc53ef7ec2a4aded51296e08fea9e2b5a736ee62d63dbea45e8ca9671282fafb69da92728b1a71de0a9e060b2905d6a5b67ecd3b3692ddbd7f2d778b8c9803aee328091b58fab324e4fad675945585808b4831d7bc3ff4def08e4b7a9de576d26586cec64b6116");
		byte[] tag = Hex.decode("1ae10b594f09e26a7e902ecbd0600691");

		final int MAC_LEN = 16;
		AEADParameters params = new AEADParameters(new KeyParameter(key), MAC_LEN * 8, nonce, aad);

		AeadChaCha20Poly1305Engine aeadEngine = new AeadChaCha20Poly1305Engine();
		byte[] aead = aeadEngine.encrypt(params, plaintext);

		byte[] encryptedTag = new byte[MAC_LEN];
		byte[] encryptedCiphertext = new byte[plaintext.length];
		System.arraycopy(aead, 0, encryptedTag, 0, MAC_LEN);
		System.arraycopy(aead, MAC_LEN, encryptedCiphertext, 0, aead.length - MAC_LEN);

		Assert.assertArrayEquals(ciphertext, encryptedCiphertext);
		Assert.assertArrayEquals(tag, encryptedTag);

		byte[] decryptedAead = aeadEngine.decrypt(params, encryptedCiphertext);

		byte[] decryptedTag = new byte[MAC_LEN];
		byte[] decryptedPlaintext = new byte[plaintext.length];
		System.arraycopy(decryptedAead, 0, decryptedTag, 0, MAC_LEN);
		System.arraycopy(decryptedAead, MAC_LEN, decryptedPlaintext, 0, decryptedAead.length - MAC_LEN);

		Assert.assertArrayEquals(tag, decryptedTag);
		Assert.assertArrayEquals(plaintext, decryptedPlaintext);
	}
}
