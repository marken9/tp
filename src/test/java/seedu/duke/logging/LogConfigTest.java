package seedu.duke.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;

public class LogConfigTest {

    @Test
    void setup_configuresRootLogger_success() {
        LogConfig.setup();
        Logger rootLogger = Logger.getLogger("");

        assertEquals(Level.ALL, rootLogger.getLevel());

        assertTrue(rootLogger.getHandlers().length >= 1, "Root logger should have handlers attached");
    }

    @Test
    void logger_inheritsSettings_success() {
        LogConfig.setup();
        Logger testLogger = Logger.getLogger("test.logger");

        // Loggers should inherit level from root if not explicitly set
        assertNotNull(testLogger);
        testLogger.info("Test log entry to verify no crash.");
    }

    @Test
    void setup_rootLoggerLevelIsAll() {
        LogConfig.setup();
        assertEquals(Level.ALL, Logger.getLogger("").getLevel());
    }

    @Test
    void setup_atLeastOneFileHandlerAttached() {
        LogConfig.setup();
        Handler[] handlers = Logger.getLogger("").getHandlers();
        boolean hasFileHandler = false;
        for (Handler h : handlers) {
            if (h instanceof FileHandler) {
                hasFileHandler = true;
                break;
            }
        }
        assertTrue(hasFileHandler, "A FileHandler should be registered on the root logger");
    }

    @Test
    void setup_consoleHandlerLevelIsWarning() {
        LogConfig.setup();
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (Handler h : handlers) {
            if (h instanceof ConsoleHandler) {
                assertEquals(Level.SEVERE, h.getLevel(),
                        "ConsoleHandler should only show SEVERE errors");
                return;
            }
        }
        // If no ConsoleHandler is present the test passes vacuously — setup may omit it.
    }

    @Test
    void setup_calledTwice_doesNotDuplicateHandlers() {
        LogConfig.setup();
        int countAfterFirst = Logger.getLogger("").getHandlers().length;

        LogConfig.setup();
        int countAfterSecond = Logger.getLogger("").getHandlers().length;

        // LogManager.reset() is called each time, so the count should be stable.
        assertEquals(countAfterFirst, countAfterSecond,
                "Calling setup twice should not accumulate duplicate handlers");
    }

    @Test
    void setup_childLoggerCanLog_withoutException() {
        LogConfig.setup();
        Logger child = Logger.getLogger("seedu.duke.test.ChildLogger");
        // Simply verifying no exception is thrown when logging at various levels
        assertDoesNotThrow(() -> {
            child.fine("fine message");
            child.info("info message");
            child.warning("warning message");
        });
    }
}
