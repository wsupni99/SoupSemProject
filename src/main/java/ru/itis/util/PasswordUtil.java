package ru.itis.util;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    public static String hash(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    public static boolean check(String plainPassword, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hash);
        return result.verified;
    }

    public static boolean matches(String plainPassword, String hash) {
        return check(plainPassword, hash);
    }
}