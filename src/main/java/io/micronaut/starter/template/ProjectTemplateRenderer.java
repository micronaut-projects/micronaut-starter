package io.micronaut.starter.template;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectTemplateRenderer implements TemplateRenderer {

    private final Map<String, String> replacements;
    private final OutputHandler outputHandler;

    public ProjectTemplateRenderer(Project project,
                                   OutputHandler outputHandler) {
        replacements = project.getProperties();
        this.outputHandler = outputHandler;
    }

    @Override
    public void render(Template template) throws IOException {
        String path = replaceVariables(template.getPath(), replacements);
        outputHandler.write(path, template);
    }

    public void render(Template template, Consumer<String> onSuccess) throws IOException {
        String path = replaceVariables(template.getPath(), replacements);
        outputHandler.write(path, template);
        if (onSuccess != null) {
            onSuccess.accept(path);
        }
    }

    protected String replaceVariables(String path, Map<String, String> replacements) {
        Pattern pattern = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(path);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            builder.append(path, i, matcher.start());
            if (replacement == null)
                builder.append(matcher.group(0));
            else
                builder.append(replacement);
            i = matcher.end();
        }
        builder.append(path.substring(i));
        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        outputHandler.close();
    }
}
