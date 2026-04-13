package seedu.duke.util;

import seedu.duke.exception.IllegalDateException;
import seedu.duke.tasklist.Category;
import seedu.duke.tasklist.CategoryList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.logging.Logger;


/**
 * Utility class for parsing and validating date-time strings used throughout the application.
 *
 * <p>Supports two input formats:
 * {@code dd-MM-yyyy HHmm} — full date and time
 * {@code dd-MM-yyyy} — date only; time defaults to {@code 23:59}
 *
 * <p>This class cannot be instantiated.
 */
public class DateUtils {
    private static final Logger logger = Logger.getLogger(DateUtils.class.getName());
    private static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter
            .ofPattern("dd-MM-uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter
            .ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FULL_FORMATTER_LENIENT = DateTimeFormatter
            .ofPattern("dd-MM-uuuu HHmm")
            .withResolverStyle(ResolverStyle.LENIENT);

    private static final DateTimeFormatter DATE_ONLY_FORMATTER_LENIENT = DateTimeFormatter
            .ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.LENIENT);
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    /**
     * Parses a date-time string, with optional past-date validation.
     *
     * <p>Attempts to parse {@code input} as a full date-time first, then
     * falls back to date-only (defaulting the time to {@code 23:59}).
     * If {@code isLoading} is {@code false}, the parsed date is additionally
     * validated to ensure it does not lie in the past.
     * Year-range validation is always applied regardless of {@code isLoading}.
     *
     * @param input     the date string to parse; must not be {@code null} or blank
     * @param isLoading {@code true} when reading from a saved file, which skips
     *                  past-date validation to allow previously saved dates to load
     *                  without error
     * @return the parsed {@link LocalDateTime}
     * @throws IllegalDateException if {@code input} is empty, does not match any
     *                              supported format, fails past-date validation,
     *                              or falls outside the allowed year range
     */
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
                if (isValidFormat(trimmedInput)) {
                    throw new IllegalDateException("Invalid date! That date does not exist (e.g. Nov has 30 days, " +
                            "check leap years for Feb 29).");
                }
                throw new IllegalDateException("Invalid format! Use dd-MM-yyyy HHmm or dd-MM-yyyy");
            }
        }

        if (!isLoading) {
            validateNotPast(parsedDate, trimmedInput);
        }

        validateYearRange(parsedDate, trimmedInput);

        return parsedDate;
    }

    private static boolean isValidFormat(String input) {
        try {
            LocalDateTime.parse(input, FULL_FORMATTER_LENIENT);
            return true;
        } catch (DateTimeParseException e) {
            try {
                LocalDate.parse(input, DATE_ONLY_FORMATTER_LENIENT);
                return true;
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
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

    public static LocalDate parseLocalDateNoValidation(String input) throws IllegalDateException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalDateException("Date input cannot be empty.");
        }
        String trimmed = input.trim();
        try {
            return LocalDateTime.parse(trimmed, FULL_FORMATTER).toLocalDate();
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(trimmed, DATE_ONLY_FORMATTER);
            } catch (DateTimeParseException e2) {
                if (isValidFormat(trimmed)) {
                    throw new IllegalDateException("Invalid date! That date does not exist " +
                            "(e.g. Nov has 30 days, check leap years for Feb 29).");
                }
                throw new IllegalDateException("Invalid format! Use dd-MM-yyyy HHmm or dd-MM-yyyy");
            }
        }
    }

    /**
     * Checks that no existing deadline or event falls beyond the proposed end year.
     * Call this before committing a reduction to the end year setting.
     */
    public static void validateEndYearReduction(CategoryList categories, int year) {
        for (int i = 0; i < categories.getAmount(); i++) {
            Category cat = categories.getCategory(i);

            for (int j = 0; j < cat.getDeadlineList().getSize(); j++) {
                int deadlineYear = cat.getDeadlineList().get(j).getBy().getYear();
                if (deadlineYear > year) {
                    throw new IllegalArgumentException(
                            "Cannot reduce end year to " + year + ": deadline '"
                                    + cat.getDeadlineList().get(j).getDescription()
                                    + "' is scheduled in " + deadlineYear + "."
                    );
                }
            }

            for (int j = 0; j < cat.getEventList().getSize(); j++) {
                int eventYear = cat.getEventList().get(j).getTo().getYear();
                if (eventYear > year) {
                    throw new IllegalArgumentException(
                            "Cannot reduce end year to " + year + ": event '"
                                    + cat.getEventList().get(j).getDescription()
                                    + "' runs until " + eventYear + "."
                    );
                }
            }
        }
    }

    //@@author sushmiithaa
    private static LocalDateTime parseRecurringTime(LocalDate today, String dayOfWeek,
                                                    String time, boolean isStart) throws IllegalDateException {

        LocalDateTime dateTime;
        LocalDate date = today.with(TemporalAdjusters.nextOrSame(
                DayOfWeek.valueOf(dayOfWeek.toUpperCase())));

        if (!isStart && (time == null || time.isBlank())) {
            LocalDateTime addEndTime = LocalDateTime.of(date, LocalTime.of(23, 59));
            validateYearRange(addEndTime, addEndTime.format(FULL_FORMATTER));
            return addEndTime;
        }
        try {
            dateTime = LocalDateTime.of(date, LocalTime.parse(time, TIME_ONLY_FORMATTER));
        } catch (DateTimeParseException e) {
            if (time.matches("\\d{4}")) {
                throw new IllegalDateException("Invalid" + (isStart ? " start " : " end ") + "time '" + time
                        + "'. That time does not exist (hours 00-23, minutes 00-59).");
            }
            throw new IllegalDateException("Invalid" + (isStart ? " start " : " end ") + "time '" + time + "'. " +
                    "Use 4-digit format e.g. 1600");
        }
        validateYearRange(dateTime, dateTime.format(FULL_FORMATTER));
        return dateTime;
    }

    public static LocalDateTime parseRecurringTimeFrom(LocalDate today,
                                                       String dayOfWeek, String time) throws IllegalDateException {
        return parseRecurringTime(today, dayOfWeek, time, true);
    }

    public static LocalDateTime parseRecurringTimeTo(LocalDate today,
                                                     String dayOfWeek, String time) throws IllegalDateException {
        return parseRecurringTime(today, dayOfWeek, time, false);
    }

    private static void parseDay(String dayOfWeek, boolean isStart) throws IllegalDateException {
        try {
            DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalDateException("Invalid" + (isStart ? " start " : " end ") + "day '" + dayOfWeek + "'. " +
                    "Ensure that the date format is EEEE HHmm"
                    + " where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
        }
    }

    public static void parseRecurringDayFrom(String dayOfWeek) throws IllegalDateException {
        parseDay(dayOfWeek, true);
    }

    public static void parseRecurringDayTo(String dayOfWeek) throws IllegalDateException {
        parseDay(dayOfWeek, false);
    }
}
