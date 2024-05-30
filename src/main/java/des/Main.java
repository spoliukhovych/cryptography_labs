package des;

import java.util.ArrayList;
import java.util.List;

import static des.implementation.DESByLib.decrypt;
import static des.implementation.DESByLib.encrypt;
import static des.implementation.DESByMine.*;

public class Main {
    public static void main(String[] args) {
        //setting the desired bit pattern for the IV used in the CBC mode of operation
        List<Integer> IV = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            IV.add(0);
        }
        for (int i = 0; i < 20; i++) {
            IV.add(1);
        }
        for (int i = 0; i < 4; i++) {
            IV.add(0);
        }

        String text = "Secret Test Message";
        String KEY = "1f4f8b113b4a5d66";

        List<Integer> cypherText = cbcDesEnc(IV, text, KEY);
        String originalText = cbcDesDec(IV, cypherText, KEY);
        String originalText_withoutEmptyBlocks = cbcDesDecRemoveEmptyBlocks(IV, cypherText, KEY);

        System.out.println("Encrypted text by mine: " + listToString(cypherText));
        System.out.println("Decrypted text by mine: " + originalText);
        System.out.println("Decrypted text by mine: " + originalText_withoutEmptyBlocks + "\n");

        try {
            byte[] key = hexToBytes(KEY);
            byte[] plaintext = text.getBytes("UTF-8");

            // Encrypt the plaintext using the key
            byte[] ciphertext = encrypt(plaintext, key);
            System.out.println("Encrypted text by lib : " + new String(ciphertext));

            // Decrypt the ciphertext using the key
            byte[] decryptedText = decrypt(ciphertext, key);
            System.out.println("Decrypted text by lib : " + new String(decryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] hexToBytes(String hexString) {
        int length = hexString.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
}
