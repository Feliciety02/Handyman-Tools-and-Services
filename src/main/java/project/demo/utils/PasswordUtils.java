package project.demo.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBcryptHash(String password) {
        return password != null && password.startsWith("$2a$") || 
               (password != null && password.startsWith("$2b$")) ||
               (password != null && password.startsWith("$2y$"));
    }
}
