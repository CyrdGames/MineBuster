package data;

import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authentication {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int SALT_SIZE = 32;
    private static final int HASH_SIZE = 32;
    private static int ITERATIONS = 11111;

    public static String createHash(char[] pass) {
        char[] passArray;
        byte[] salt, hash;

        ITERATIONS = 11111 + (int) (Math.random() * 11111);

        passArray = pass;
        salt = new byte[SALT_SIZE];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        hash = getHash(passArray, salt);

        return format(salt, hash);
    }

    private static String format(byte[] salt, byte[] hash) {
        Base64.Encoder encoder = Base64.getEncoder();
        return ITERATIONS + ":" + encoder.encodeToString(salt) + ":" + encoder.encodeToString(hash);
    }

    private static byte[] getHash(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_SIZE * 8);
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new byte[HASH_SIZE];
    }

    public static boolean checkPassword(char[] password, String hash) {
        String[] secret = hash.split(":");
        ITERATIONS = Integer.parseInt(secret[0]);
        byte[] byteHash = getHash(password, Base64.getDecoder().decode(secret[1]));
        String pass = Base64.getEncoder().encodeToString(byteHash);
        return pass.equals(secret[2]);
    }
}
