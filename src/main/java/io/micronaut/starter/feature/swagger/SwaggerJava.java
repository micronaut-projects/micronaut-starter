package io.micronaut.starter.feature.swagger;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.lang.java.JavaApplication;
import io.micronaut.starter.template.RockerTemplate;

public class SwaggerJava extends JavaApplication {

    @Override
    public String getName() {
        return "swagger-java";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("application", new RockerTemplate(getPath(), java.template(commandContext.getProject())));
    }
}
