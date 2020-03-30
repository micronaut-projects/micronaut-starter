package io.micronaut.starter.feature.logging;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.feature.logging.template.log4j2;

import javax.inject.Singleton;

@Singleton
public class Log4j2 implements LoggingFeature {

    @Override
    public String getName() {
        return "log4j2";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/log4j2.xml", log4j2.template(commandContext.getProject())));
    }
}
