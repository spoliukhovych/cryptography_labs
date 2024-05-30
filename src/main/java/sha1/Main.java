package sha1;

import sha1.implementation.SHA1ByLib;

import static sha1.implementation.SHA1ByMine.bytesToHex;
import static sha1.implementation.SHA1ByMine.calculateSHA1;

public class Main {
    public static void main(String[] args) {

        String message = "Hello, World!";
        byte[] sha1Hash = calculateSHA1(message.getBytes());
        String sha1HashHex_m = bytesToHex(sha1Hash);
        String sha1HashHex_l = null;
        try {
            sha1HashHex_l = SHA1ByLib.sha1(message);
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("SHA-1 hash mine: " + sha1HashHex_m);
        System.out.println("SHA-1 hash lib:  " + sha1HashHex_l);
    }

}
