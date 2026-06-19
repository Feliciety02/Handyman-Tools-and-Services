package project.demo.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(09\\d{9}|\\+639\\d{9}|0\\d{9,10})$");
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_ ]{3,50}$");
    private static final Pattern POSTAL_CODE_PATTERN = 
        Pattern.compile("^\\d{4,10}$");
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-ZÀ-ÿ\\s'.\\-]{2,100}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && 
               password.length() <= 128 &&
               Pattern.compile(".*[A-Z].*").matcher(password).find() &&
               Pattern.compile(".*[a-z].*").matcher(password).find() &&
               Pattern.compile(".*\\d.*").matcher(password).find();
    }

    public static boolean isValidPostalCode(String code) {
        return code != null && POSTAL_CODE_PATTERN.matcher(code.trim()).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static String sanitize(String input) {
        if (input == null) return "";
        return input.trim()
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;")
            .replaceAll("'", "&#x27;")
            .replaceAll("&(?!lt;|gt;|quot;|#x27;)", "&amp;");
    }

    public static boolean hasMinimumLength(String value, int min) {
        return value != null && value.trim().length() >= min;
    }
}
