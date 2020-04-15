package io.micronaut.starter.io;

import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.template.Template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapOutputHandler implements OutputHandler {

    private final Map<String, String> templates;

    public MapOutputHandler() {
        templates = new LinkedHashMap<>(16);
    }

    @Override
    public boolean exists(String path) {
        return templates.containsKey(path);
    }

    @Override
    public void write(String path, Template contents) throws IOException {
        if (contents.isBinary()) {
            templates.put(path, null);
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            contents.write(out);
            templates.put(path, out.toString(StandardCharsets.UTF_8.name()));
        }
    }

    @Override
    public String getOutputLocation() {
        return null;
    }

    @Override
    public void close() throws IOException { }

    public Map<String, String> getProject() {
        return templates;
    }
}
