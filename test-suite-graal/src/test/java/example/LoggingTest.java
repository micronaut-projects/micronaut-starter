package example;

import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoggingTest {

    private static final ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
    private static final LoggerContext context = loggerFactory instanceof LoggerContext ? ((LoggerContext) loggerFactory) : null;

    private final PrintStream systemOut = System.out;

    private ByteArrayOutputStream byteArrayOutputStream;

    @BeforeEach
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(systemOut);
    }

    @Test
    void testConsoleAppender() {
        assertNotNull(context);
        Logger logger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.info("test message");
        String result = byteArrayOutputStream.toString();
        assertTrue( result + "should contain test message", result.contains("test message"));
    }
}
