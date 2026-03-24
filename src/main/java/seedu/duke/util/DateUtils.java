package seedu.duke.util;

import seedu.duke.exception.IllegalDateException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.logging.Logger;

public class DateUtils {
    private static final Logger logger = Logger.getLogger(DateUtils.class.getName());
    private static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

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
            throw new IllegalDateException("Invalid" + (isStart ? " start " : " end ") + "time '" + time + "'. " +
                    "Use 4-digit format e.g. 1600");
        }
        validateYearRange(dateTime, dateTime.format(FULL_FORMATTER));
        return dateTime;
    }

    public static LocalDateTime parseRecurringTimeFrom(LocalDate today,
            String dayOfWeek, String time) throws IllegalDateException {
        return parseRecurringTime(today, dayOfWeek, time,true);
    }

    public static LocalDateTime parseRecurringTimeTo(LocalDate today,
            String dayOfWeek, String time) throws IllegalDateException {
        return parseRecurringTime(today, dayOfWeek, time,false);
    }

    private static void parseDay(String dayOfWeek,boolean isStart) throws IllegalDateException {
        try {
            DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalDateException("Invalid" + (isStart ? " start " : " end ") + "day '" + dayOfWeek + "'. " +
                    "Ensure that the date format is EEEE HHmm"
                    + " where EEEE is 'Monday','Tuesday', 'Wednesday', 'Thursday','Friday','Saturday','Sunday'");
        }
    }

    public static void parseRecurringDayFrom(String dayOfWeek) throws IllegalDateException {
        parseDay(dayOfWeek,true);
    }

    public static void parseRecurringDayTo(String dayOfWeek) throws IllegalDateException {
        parseDay(dayOfWeek,false);
    }
}
