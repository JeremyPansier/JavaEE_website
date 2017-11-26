package com.website.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

    private Hasher() {
        super();
    }

    public static byte[] hash(String algorithme, String monMessage) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(algorithme);
        return sha.digest(monMessage.getBytes());
    }

    public static String bytesToHex(byte[] b) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        StringBuilder buffer = new StringBuilder();
        for (int j = 0; j < b.length; j++) {
            buffer.append(hexDigits[(b[j] >> 4) & 0x0f]);
            buffer.append(hexDigits[b[j] & 0x0f]);
        }
        return buffer.toString();
    }

    public static String hashToHex(String algorithme, String monMessage) throws NoSuchAlgorithmException {
        return bytesToHex(hash(algorithme, monMessage));
    }

    public static String sha1ToHex(String monMessage) {
        try {
            return hashToHex("SHA-1", monMessage);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
