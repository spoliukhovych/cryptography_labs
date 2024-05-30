package sha1.implementation;

public class SHA1ByMine {
    public static byte[] calculateSHA1(byte[] message) {
        int[] hash = {0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0};

        int ml = message.length * 8; // Message length in bits
        message = appendPadding(message);

        // Process the message in 512-bit blocks
        for (int i = 0; i < message.length; i += 64) {
            int[] words = new int[80];
            for (int j = 0; j < 16; j++) {
                words[j] = bytesToInt(message, i + j * 4);
            }
            for (int j = 16; j < 80; j++) {
                words[j] = leftRotate(words[j - 3] ^ words[j - 8] ^ words[j - 14] ^ words[j - 16], 1);
            }

            int a = hash[0];
            int b = hash[1];
            int c = hash[2];
            int d = hash[3];
            int e = hash[4];

            // Main hash computation
            for (int j = 0; j < 80; j++) {
                int f, k;
                if (j < 20) {
                    f = (b & c) | (~b & d);
                    k = 0x5A827999;
                } else if (j < 40) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                } else if (j < 60) {
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                } else {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }

                int temp = leftRotate(a, 5) + f + e + k + words[j];
                e = d;
                d = c;
                c = leftRotate(b, 30);
                b = a;
                a = temp;
            }

            // Update the hash values
            hash[0] += a;
            hash[1] += b;
            hash[2] += c;
            hash[3] += d;
            hash[4] += e;
        }

        byte[] result = new byte[20];
        for (int i = 0; i < 5; i++) {
            intToBytes(hash[i], result, i * 4);
        }
        return result;
    }

    private static byte[] appendPadding(byte[] message) {
        int paddingBytes = 64 - (message.length % 64);
        if (paddingBytes < 9) {
            paddingBytes += 64;
        }

        byte[] paddedMessage = new byte[message.length + paddingBytes];
        System.arraycopy(message, 0, paddedMessage, 0, message.length);

        paddedMessage[message.length] = (byte) 0x80;

        long messageLengthBits = (long) message.length * 8;
        for (int i = 0; i < 8; i++) {
            paddedMessage[paddedMessage.length - 8 + i] = (byte) ((messageLengthBits >> (56 - i * 8)) & 0xFF);
        }

        return paddedMessage;
    }

    private static int leftRotate(int value, int count) {
        return (value << count) | (value >>> (32 - count));
    }

    private static int bytesToInt(byte[] bytes, int offset) {
        return (bytes[offset] & 0xFF) << 24 |
                (bytes[offset + 1] & 0xFF) << 16 |
                (bytes[offset + 2] & 0xFF) << 8 |
                (bytes[offset + 3] & 0xFF);
    }

    private static void intToBytes(int value, byte[] bytes, int offset) {
        bytes[offset] = (byte) (value >>> 24);
        bytes[offset + 1] = (byte) (value >>> 16);
        bytes[offset + 2] = (byte) (value >>> 8);
        bytes[offset + 3] = (byte) value;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

