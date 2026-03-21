package seedu.duke.util;

import seedu.duke.exception.IllegalDateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class DateUtils {
    private static final Logger logger = Logger.getLogger(DateUtils.class.getName());
    private static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static LocalDateTime parse(String input, boolean isLoading) throws IllegalDateException {
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
                throw new IllegalDateException("Invalid format! Use dd-MM-yyyy HHmm or dd-MM-yyyy");
            }
        }

        if (!isLoading) {
            validateNotPast(parsedDate,trimmedInput);
        }

        validateYearRange(parsedDate, trimmedInput);

        return parsedDate;
    }

    public static LocalDateTime parseDateTime(String input) throws IllegalDateException {
        return parse(input, false);
    }

    public static LocalDateTime parseDateTimeFromFile(String input) throws IllegalDateException {
        return parse(input, true);
    }

    private static void validateYearRange(LocalDateTime parsedDate, String originalInput)
            throws IllegalDateException {
        int startYear = LocalDate.now().getYear();
        int endYear = seedu.duke.UniTasker.getEndYear();
        int taskYear = parsedDate.getYear();

        if (taskYear < startYear || taskYear > endYear) {
            logger.warning("Rejected date " + originalInput + " - Year out of range ("
                    + startYear + "-" + endYear + ").");
            throw new IllegalDateException("Dates must be between " + startYear + " and " + endYear + "!");
        }
    }

    private static void validateNotPast(LocalDateTime parsedDate, String originalInput) throws IllegalDateException {
        if (parsedDate.isBefore(LocalDateTime.now())) {
            throw new IllegalDateException("Cannot schedule tasks in the past! (" + originalInput + ") ");
        }
    }

    public static LocalDate parseLocalDate(String input) throws IllegalDateException {
        return parseDateTime(input).toLocalDate();
    }
}
