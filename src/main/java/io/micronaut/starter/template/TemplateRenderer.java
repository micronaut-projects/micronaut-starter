package io.micronaut.starter.template;

import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface TemplateRenderer extends Closeable {

    void render(Template template) throws IOException;

    void render(Template template, Consumer<String> onSuccess) throws IOException;

    static TemplateRenderer create(Project project, OutputHandler outputHandler) {
        return new ProjectTemplateRenderer(project, outputHandler);
    }
}
