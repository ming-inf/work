package cryptography;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.ChaCha7539Engine;
import org.bouncycastle.crypto.macs.Poly1305;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Pack;

public class AeadChaCha20Poly1305Engine extends ChaCha7539Engine {
  final byte[] ZEROS = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

  protected byte[] pad16(byte[] data) {
    if (data.length % 16 == 0) {
      return new byte[0];
    } else {
      int size = 16 - (data.length % 16);
      byte[] result = new byte[size];
      System.arraycopy(ZEROS, 0, result, 0, size);
      return result;
    }
  }

  final int OTKSize = 32;

  protected byte[] encrypt(AEADParameters aeadParameters, byte[] plaintext) throws IOException {
    KeyParameter key = aeadParameters.getKey();
    byte[] nonce = aeadParameters.getNonce();
    byte[] aad = aeadParameters.getAssociatedText();

    byte[] otk = new byte[OTKSize];
    init(true, new ParametersWithIV(key, nonce));
    byte[] keyStream = new byte[64];
    generateKeyStream(keyStream);
    System.arraycopy(keyStream, 0, otk, 0, OTKSize);

    reset();

    byte[] ciphertext = new byte[plaintext.length];
    init(true, new ParametersWithIV(key, nonce));
    skip(64);
    processBytes(plaintext, 0, plaintext.length, ciphertext, 0);

    ByteArrayOutputStream macData = new ByteArrayOutputStream();
    macData.write(aad);
    macData.write(pad16(aad));
    macData.write(ciphertext);
    macData.write(pad16(ciphertext));
    macData.write(Pack.longToLittleEndian(aad.length));
    macData.write(Pack.longToLittleEndian(ciphertext.length));

    Mac mac = new Poly1305();
    mac.init(new KeyParameter(otk));
    mac.update(macData.toByteArray(), 0, macData.toByteArray().length);

    byte[] tag = new byte[mac.getMacSize()];
    mac.doFinal(tag, 0);

    ByteArrayOutputStream aead = new ByteArrayOutputStream();
    aead.write(tag);
    aead.write(ciphertext);

    return aead.toByteArray();
  }

  protected byte[] decrypt(AEADParameters aeadParameters, byte[] ciphertext) throws IOException {
    KeyParameter key = aeadParameters.getKey();
    byte[] nonce = aeadParameters.getNonce();
    byte[] aad = aeadParameters.getAssociatedText();

    byte[] otk = new byte[OTKSize];
    init(true, new ParametersWithIV(key, nonce));
    byte[] keyStream = new byte[64];
    generateKeyStream(keyStream);
    System.arraycopy(keyStream, 0, otk, 0, OTKSize);

    reset();

    byte[] plaintext = new byte[ciphertext.length];
    init(true, new ParametersWithIV(key, nonce));
    skip(64);
    processBytes(ciphertext, 0, ciphertext.length, plaintext, 0);

    ByteArrayOutputStream macData = new ByteArrayOutputStream();
    macData.write(aad);
    macData.write(pad16(aad));
    macData.write(ciphertext);
    macData.write(pad16(ciphertext));
    macData.write(Pack.longToLittleEndian(aad.length));
    macData.write(Pack.longToLittleEndian(plaintext.length));

    Mac mac = new Poly1305();
    mac.init(new KeyParameter(otk));
    mac.update(macData.toByteArray(), 0, macData.size());

    byte[] tag = new byte[mac.getMacSize()];
    mac.doFinal(tag, 0);

    ByteArrayOutputStream aead = new ByteArrayOutputStream();
    aead.write(tag);
    aead.write(plaintext);

    return aead.toByteArray();
  }
}
