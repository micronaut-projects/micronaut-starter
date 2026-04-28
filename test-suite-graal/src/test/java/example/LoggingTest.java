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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoggingTest {
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
        ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
        assertNotNull(loggerFactory);
        assertInstanceOf(LoggerContext.class, loggerFactory, "LoggerFactory is not a LoggerContext it is of type " + loggerFactory.getClass().getName());
        LoggerContext context = (LoggerContext) loggerFactory;
        assertNotNull(context);
        Logger logger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.info("test message");
        String result = byteArrayOutputStream.toString();
        assertNotNull(result);
        assertTrue(result.contains("test message"), result);
    }
}
