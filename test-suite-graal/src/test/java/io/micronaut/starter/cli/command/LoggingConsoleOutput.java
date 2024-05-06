package io.micronaut.starter.cli.command;

import io.micronaut.starter.io.ConsoleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingConsoleOutput implements ConsoleOutput {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingConsoleOutput.class);

    @Override
    public void out(String message) {
        LOG.info(message);
    }

    @Override
    public void err(String message) {
        LOG.error(message);
    }

    @Override
    public void warning(String message) {
        LOG.warn(message);
    }

    @Override
    public boolean showStacktrace() {
        return false;
    }

    @Override
    public boolean verbose() {
        return false;
    }
}
