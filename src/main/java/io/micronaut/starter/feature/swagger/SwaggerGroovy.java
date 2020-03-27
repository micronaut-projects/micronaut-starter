package io.micronaut.starter.feature.swagger;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.lang.groovy.GroovyApplication;
import io.micronaut.starter.template.RockerTemplate;

public class SwaggerGroovy extends GroovyApplication {

    @Override
    public String getName() {
        return "swagger-groovy";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("application", new RockerTemplate(getPath(), java.template(commandContext.getProject())));
    }

}
