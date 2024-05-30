package des.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DESByMine {
    private static final int[] ip = {57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7,
            56, 48, 40, 32, 24, 16, 8, 0,
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6};

    private static final int[] expansionTable = {31, 0, 1, 2, 3, 4,
            3, 4, 5, 6, 7, 8,
            7, 8, 9, 10, 11, 12,
            11, 12, 13, 14, 15, 16,
            15, 16, 17, 18, 19, 20,
            19, 20, 21, 22, 23, 24,
            23, 24, 25, 26, 27, 28,
            27, 28, 29, 30, 31, 0};

    private static final int[] pc1 = {56, 48, 40, 32, 24, 16, 8, 0,
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 60, 52, 44, 36,
            28, 20, 12, 4, 27, 19, 11, 3};

    private static final int[] leftRotations = {1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1};

    private static final int[] pc2 = {13, 16, 10, 23, 0, 4,
            2, 27, 14, 5, 20, 9,
            22, 18, 11, 3, 25, 7,
            15, 6, 26, 19, 12, 1,
            40, 51, 30, 36, 46, 54,
            29, 39, 50, 44, 32, 47,
            43, 48, 38, 55, 33, 52,
            45, 41, 49, 35, 28, 31
    };
    private static final int[] fp = {
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25,
            32, 0, 40, 8, 48, 16, 56, 24
    };
    private static final int[][] sbox = {
            // S1
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},

            // S2
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},

            // S3
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12},

            // S4
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14},

            // S5
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},

            // S6
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},

            // S7
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},

            // S8
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11},
    };
    private static final int[] p = {
            15, 6, 19, 20, 28, 11,
            27, 16, 0, 14, 22, 25,
            4, 17, 30, 9, 1, 7,
            23, 13, 31, 26, 2, 8,
            18, 12, 29, 5, 21, 10,
            3, 24
    };
    public static String listToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i : list) {
            sb.append(i);
        }
        return sb.toString();
    }
    private static String hexToBinary(String hexString) {
        StringBuilder binaryString = new StringBuilder();
        for (int i = 0; i < hexString.length(); i++) {
            String binary = Integer.toBinaryString(Integer.parseInt(hexString.charAt(i) + "", 16));
            while (binary.length() < 4) {
                binary = "0" + binary;
            }
            binaryString.append(binary);
        }
        return binaryString.toString();
    }
    private static String charToBinary(char c) {
        String binary = Integer.toBinaryString(c);
        while (binary.length() < 8) {
            binary = "0" + binary;
        }
        return binary;
    }
    public static List<Integer> keyGeneration(String key_64) {
        List<Integer> subkeys = new ArrayList<>();
        String key_64_p = "";
        for (int i = 0; i < 56; i++) {
            key_64_p += key_64.charAt(pc1[i]);
        }

        String c0 = key_64_p.substring(0, 28);
        String d0 = key_64_p.substring(28, 56);

        for (int j = 0; j < 16; j++) {
            for (int i = 0; i < leftRotations[j]; i++) {
                String c1 = c0.substring(1) + c0.charAt(0);
                String d1 = d0.substring(1) + d0.charAt(0);
                c0 = c1;
                d0 = d1;
            }
            String tab_pc2 = c0 + d0;

            String res_pc2 = "";
            for (int i = 0; i < 48; i++) {
                res_pc2 += tab_pc2.charAt(pc2[i]);
            }

            for (int i = 0; i < 48; i++) {
                subkeys.add(Character.getNumericValue(res_pc2.charAt(i)));
            }
        }

        return subkeys;
    }
    public static List<Integer> f(List<Integer> R, List<Integer> K) {
        List<Integer> R48 = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            R48.add(R.get(expansionTable[i]));
        }

        List<Integer> after_XOR = new ArrayList<>();
        for (int j = 0; j < 48; j++) {
            after_XOR.add(R48.get(j) ^ K.get(j));
        }

        List<Integer> after_sbox = new ArrayList<>();
        for (int j = 0; j < 8; j++) {
            List<Integer> Sixbits = after_XOR.subList(j * 6, (j + 1) * 6);
            int bits_1_6 = Integer.parseInt(Sixbits.get(0) + "" + Sixbits.get(5), 2);
            int bits_2_5 = Integer.parseInt(Sixbits.get(1) + "" + Sixbits.get(2) + "" + Sixbits.get(3) + "" + Sixbits.get(4), 2);
            int found_int = sbox[j][bits_1_6 * 16 + bits_2_5];
            String binaryString = Integer.toBinaryString(found_int);
            while (binaryString.length() < 4) {
                binaryString = "0" + binaryString;
            }
            for (int i = 0; i < binaryString.length(); i++) {
                after_sbox.add(Character.getNumericValue(binaryString.charAt(i)));
            }
        }

        List<Integer> result = new ArrayList<>(after_sbox);
        for (int i = 0; i < 32; i++) {
            result.set(i, after_sbox.get(p[i]));
        }

        return result;
    }
    public static List<Integer> des(List<Integer> plaintext_64, List<Integer> key_64) {
        List<Integer> subkeys = keyGeneration(listToString(key_64));
        List<Integer> iptext = new ArrayList<>(plaintext_64);

        for (int i = 0; i < 64; i++) {
            iptext.set(i, plaintext_64.get(ip[i]));
        }
        List<Integer> L = iptext.subList(0, 32);
        List<Integer> R = iptext.subList(32, 64);

        for (int i = 0; i < 16; i++) {
            List<Integer> f_result = f(R, subkeys.subList(i * 48, (i + 1) * 48));
            List<Integer> C = new ArrayList<>(R);
            for (int q = 0; q < 32; q++) {
                R.set(q, L.get(q) ^ f_result.get(q));
            }
            L = new ArrayList<>(C);
        }

        List<Integer> res = new ArrayList<>(R);
        res.addAll(L);
        List<Integer> fptext = new ArrayList<>(res);
        for (int i = 0; i < 64; i++) {
            fptext.set(i, res.get(fp[i]));
        }

        return fptext;
    }
    public static List<Integer> desDecrypt(List<Integer> ciphertext, List<Integer> key_64) {
        List<Integer> initial_permut = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            initial_permut.add(ciphertext.get(ip[i]));
        }

        List<Integer> L = initial_permut.subList(0, 32);
        List<Integer> R = initial_permut.subList(32, 64);
        List<Integer> subkeys = keyGeneration(listToString(key_64));

        for (int i = 15; i >= 0; i--) {
            List<Integer> f_result = f(R, subkeys.subList(i * 48, (i + 1) * 48));
            List<Integer> C = new ArrayList<>(R);
            for (int q = 0; q < 32; q++) {
                R.set(q, L.get(q) ^ f_result.get(q));
            }
            L = new ArrayList<>(C);
        }

        List<Integer> res = new ArrayList<>(R);
        res.addAll(L);

        List<Integer> final_result = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            final_result.add(res.get(fp[i]));
        }

        return final_result;
    }
    public static List<Integer> cbcDesEnc(List<Integer> IV, String text, String keyHexa) {
        List<Integer> key_binary = new ArrayList<>();
        for (char c : hexToBinary(keyHexa).toCharArray()) {
            key_binary.add(Character.getNumericValue(c));
        }

        List<Integer> binary_text_array = new ArrayList<>();
        for (char c : text.toCharArray()) {
            for (char bit : charToBinary(c).toCharArray()) {
                binary_text_array.add(Character.getNumericValue(bit));
            }
        }

        int block_number = binary_text_array.size() / 64;

        int last_block_size = binary_text_array.size() % 64;

        List<Integer> init_vect = new ArrayList<>(IV);
        List<Integer> cipher_text = new ArrayList<>();

        for (int i = 0; i < block_number; i++) {
            List<Integer> block = binary_text_array.subList(i * 64, (i + 1) * 64);
            List<Integer> XOR_res = new ArrayList<>();
            for (int j = 0; j < 64; j++) {
                XOR_res.add(init_vect.get(j) ^ block.get(j));
            }
            List<Integer> cipher_block = des(XOR_res, key_binary);
            cipher_text.addAll(cipher_block);
            init_vect = new ArrayList<>(cipher_block);
        }

        if (last_block_size != 0) {
            block_number++;
            List<Integer> last_block = new ArrayList<>(binary_text_array.subList(binary_text_array.size() - last_block_size, binary_text_array.size()));
            int paddingSize = 64 - last_block_size;
            List<Integer> padding = new ArrayList<>(Collections.nCopies(paddingSize, 0));
            last_block.addAll(padding);

            List<Integer> XOR_res = new ArrayList<>();
            for (int i = 0; i < 64; i++) {
                XOR_res.add(init_vect.get(i) ^ last_block.get(i));
            }
            List<Integer> cipher_block = des(XOR_res, key_binary);
            cipher_text.addAll(cipher_block);
        }

        List<Integer> cypher_text_ascii_string = new ArrayList<>();

        for (int i = 0; i < cipher_text.size() - 7; i += 8) {
            String binaryString = "";
            for (int j = 0; j < 8; j++) {
                binaryString += cipher_text.get(i + j);
            }
            int asciiValue = Integer.parseInt(binaryString, 2);
            cypher_text_ascii_string.add(asciiValue);
        }

        return cypher_text_ascii_string;
    }

    public static String cbcDesDec(List<Integer> IV, List<Integer> ciphertext, String keyHexa) {
        List<Integer> key_binary = new ArrayList<>();
        for (char c : hexToBinary(keyHexa).toCharArray()) {
            key_binary.add(Character.getNumericValue(c));
        }

        List<Integer> binary_ciphertext_array = new ArrayList<>();
        for (int i : ciphertext) {
            for (char bit : charToBinary((char) i).toCharArray()) {
                binary_ciphertext_array.add(Character.getNumericValue(bit));
            }
        }

        int block_number = binary_ciphertext_array.size() / 64;

        List<Integer> init_vect = new ArrayList<>(IV);
        List<Integer> text = new ArrayList<>();
        for (int i = 0; i < block_number; i++) {
            List<Integer> block = binary_ciphertext_array.subList(i * 64, (i + 1) * 64);
            List<Integer> text_block = desDecrypt(block, key_binary);
            List<Integer> XOR_res = new ArrayList<>();
            for (int j = 0; j < 64; j++) {
                XOR_res.add(init_vect.get(j) ^ text_block.get(j));
            }
            text.addAll(XOR_res);
            init_vect = new ArrayList<>(block);
        }

        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < text.size() - 7; i += 8) {
            String binaryString = "";
            for (int j = 0; j < 8; j++) {
                binaryString += text.get(i + j);
            }
            int asciiValue = Integer.parseInt(binaryString, 2);
            decryptedText.append((char) asciiValue);
        }

        return decryptedText.toString();
    }

    public static String cbcDesDecRemoveEmptyBlocks(List<Integer> IV, List<Integer> ciphertext, String keyHexa) {
        List<Integer> key_binary = new ArrayList<>();
        for (char c : hexToBinary(keyHexa).toCharArray()) {
            key_binary.add(Character.getNumericValue(c));
        }

        List<Integer> binary_ciphertext_array = new ArrayList<>();
        for (int i : ciphertext) {
            for (char bit : charToBinary((char) i).toCharArray()) {
                binary_ciphertext_array.add(Character.getNumericValue(bit));
            }
        }

        int block_number = binary_ciphertext_array.size() / 64;

        List<Integer> init_vect = new ArrayList<>(IV);
        List<Integer> text = new ArrayList<>();
        for (int i = 0; i < block_number; i++) {
            List<Integer> block = binary_ciphertext_array.subList(i * 64, (i + 1) * 64);
            List<Integer> text_block = desDecrypt(block, key_binary);
            List<Integer> XOR_res = new ArrayList<>();
            for (int j = 0; j < 64; j++) {
                XOR_res.add(init_vect.get(j) ^ text_block.get(j));
            }
            text.addAll(XOR_res);
            init_vect = new ArrayList<>(block);
        }

        StringBuilder decryptedText = new StringBuilder();
        StringBuilder currentBlock = new StringBuilder();
        for (int i = 0; i < text.size() - 7; i += 8) {
            String binaryString = "";
            for (int j = 0; j < 8; j++) {
                binaryString += text.get(i + j);
            }
            int asciiValue = Integer.parseInt(binaryString, 2);

            if (asciiValue != 0) {
                currentBlock.append((char) asciiValue);
            } else if (currentBlock.length() > 0) {
                decryptedText.append(currentBlock.toString());
                currentBlock.setLength(0);
            }
        }

        if (currentBlock.length() > 0) {
            decryptedText.append(currentBlock.toString());
        }

        return decryptedText.toString();
    }

}

