package io.micronaut.starter.cli.command;

import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

class TemplateResolvingOutputHandler implements OutputHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateResolvingOutputHandler.class);
    private final OutputStream outputStream = new NullOutputStream();

    @Override
    public boolean exists(String path) {
        return false;
    }

    @Override
    public void write(String path, Template contents) throws IOException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Writing to path: {}", path);
        }
        // Force the contents to be rendered to shake out any rendering issues
        contents.write(outputStream);
    }

    @Override
    public String getOutputLocation() {
        return "something";
    }

    @Override
    public void close() throws IOException {
    }
}
