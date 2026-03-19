package seedu.duke.util;

import seedu.duke.exception.IllegalDateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class DateUtils {
    private static final Logger logger = Logger.getLogger(DateUtils.class.getName());
    private static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime parseDateTime(String input) throws IllegalDateException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalDateException("Date input cannot be empty.");
        }

        String trimmedInput = input.trim();
        LocalDateTime parsedDate;

        try {
            parsedDate = LocalDateTime.parse(trimmedInput, FULL_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                LocalDate date = LocalDate.parse(trimmedInput, DATE_ONLY_FORMATTER);
                parsedDate = date.atTime(23, 59);
            } catch (DateTimeParseException e2) {
                throw new IllegalDateException("Invalid format! Use yyyy-MM-dd HHmm or yyyy-MM-dd");
            }
        }

        // CRITICAL CHECK
        if (parsedDate.getYear() < 2026 || parsedDate.getYear() > 2125) {
            logger.warning("Rejected date " + trimmedInput + " - Year out of allowed range (2026-2125).");
            throw new IllegalDateException("Dates must be between 2026 and 2099!");
        }

        return parsedDate;
    }

    public static LocalDate parseLocalDate(String input) throws IllegalDateException {
        // Reuse the existing logic to ensure consistency
        return parseDateTime(input).toLocalDate();
    }
}
