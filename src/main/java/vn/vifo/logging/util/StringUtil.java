package vn.vifo.logging.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtil {

    public static boolean containsString(String[] array, String target) {
        for (String s : array) {
            if (s.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static String convertLocalDateToVNDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String convertLocalTimeToVNTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return date.format(formatter);
    }

    public static LocalDateTime convertVNDateToLocalDateTime(String date) {
        if (date == null) {
            return null;
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
    }

    public static LocalDateTime convertVNTimeToLocalDateTime(String date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public static String standardizePhoneNumber(String phoneNumber) {
        // Remove spaces
        String trimmedPhoneNumber = phoneNumber.replaceAll("\\s", "");

        // Check if the number starts with "84" or "+84"
        if (trimmedPhoneNumber.startsWith("84")) {
            // Convert to format starting with "0"
            return "0" + trimmedPhoneNumber.substring(2);
        } else if (trimmedPhoneNumber.startsWith("+84")) {
            // Convert to format starting with "0"
            return "0" + trimmedPhoneNumber.substring(3);
        }

        // If the number doesn't match the specified patterns, return as is
        return trimmedPhoneNumber;
    }
}
