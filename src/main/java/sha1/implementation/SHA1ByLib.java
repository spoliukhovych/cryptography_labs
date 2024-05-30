package sha1.implementation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA1ByLib {
    public static String sha1(String input) throws Exception {
        // Convert the input string to bytes using UTF-8 encoding
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        // Create a SHA-1 message digest object
        MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");

        // Compute the hash value of the input bytes
        byte[] hashBytes = sha1Digest.digest(inputBytes);

        // Convert the hash bytes to a hexadecimal string representation
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        // Return the hexadecimal string representation of the SHA-1 hash
        return hexString.toString();
    }
}
