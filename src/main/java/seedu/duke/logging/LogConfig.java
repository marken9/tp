package seedu.duke.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Configures the application-wide logging system.
 *
 * <p>Sets up two handlers on the root logger:
 * File — appends all log records ({@link Level#ALL}) to
 * {@code unitasker.log} in the working directory using
 * {@link SimpleFormatter}
 * <p>
 * Console - prints {@link Level#WARNING} and above to
 * {@code stderr}
 *
 * <p>Call {@link #setup()} once at application startup before any
 * other component obtains a {@link Logger} instance.
 */

public class LogConfig {
    public static void setup() {
        LogManager.getLogManager().reset();
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);

        try {
            // File Handler: Saves EVERYTHING to 'unitasker.log'
            FileHandler fileHandler = new FileHandler("unitasker.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            rootLogger.addHandler(fileHandler);

            // Console handler: surfaces warnings and above during runtime
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.SEVERE);
            rootLogger.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Logging system failed to initialize: " + e.getMessage());
        }
    }
}
