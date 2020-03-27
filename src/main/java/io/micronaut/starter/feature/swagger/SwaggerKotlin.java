package io.micronaut.starter.feature.swagger;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.lang.kotlin.KotlinApplication;
import io.micronaut.starter.template.RockerTemplate;

public class SwaggerKotlin extends KotlinApplication {

    @Override
    public String getName() {
        return "swagger-kotlin";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("application", new RockerTemplate(getPath(), kotlin.template(commandContext.getProject())));
    }
}
