package des.implementation;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class DESByLib {
    private static final String ALGORITHM = "DES";
    private static final String PADDING = "PKCS7Padding";
    private static final byte[] IV = {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78,
            (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF
    };

    public static byte[] encrypt(byte[] plaintext, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // Create a PaddedBufferedBlockCipher with DES algorithm and CBC mode
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESEngine()));

        // Initialize the cipher with the encryption mode and the key and IV (Initialization Vector)
        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IV);
        cipher.init(true, params);

        // Calculate the output buffer size for the ciphertext
        byte[] ciphertext = new byte[cipher.getOutputSize(plaintext.length)];

        // Process the plaintext, performing the encryption
        int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);

        // Finalize the encryption process
        processedBytes += cipher.doFinal(ciphertext, processedBytes);

        // Return the ciphertext
        return ciphertext;
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // Create a PaddedBufferedBlockCipher with DES algorithm and CBC mode
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESEngine()));

        // Initialize the cipher with the decryption mode and the key and IV (Initialization Vector)
        CipherParameters params = new ParametersWithIV(new KeyParameter(key), IV);
        cipher.init(false, params);

        // Calculate the output buffer size for the plaintext
        byte[] plaintext = new byte[cipher.getOutputSize(ciphertext.length)];

        // Process the ciphertext, performing the decryption
        int processedBytes = cipher.processBytes(ciphertext, 0, ciphertext.length, plaintext, 0);

        // Finalize the decryption process
        processedBytes += cipher.doFinal(plaintext, processedBytes);

        // Return the plaintext
        return plaintext;
    }
}
