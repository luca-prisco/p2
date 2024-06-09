package it.unisa.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    private static final int SALT_LENGTH = 16; // Lunghezza del salt
    private static final int ITERATIONS = 10000; // Numero di iterazioni

    public static String hashPassword(String password) {
        String hashString = null;
        byte[] salt = generateSalt();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            hashString = Base64.getEncoder().encodeToString(salt) + ":" + bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashString;
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean verifyPassword(String password, String storedHash) {
        String[] parts = storedHash.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String hashOfInput = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            hashOfInput = bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashOfInput != null && hashOfInput.equals(parts[1]);
    }
}
