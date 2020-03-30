package io.micronaut.starter.feature.logging;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.logging.template.logback;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

@Singleton
public class Logback implements LoggingFeature, DefaultFeature {

    @Override
    public String getName() {
        return "logback";
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, List<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(f -> f instanceof LoggingFeature);
    }

    @Override
    public void apply(CommandContext commandContext) {
        String osName = System.getProperty("os.name");
        boolean jansi = false;
        if (osName == null || !osName.toLowerCase(Locale.ENGLISH).contains("windows")) {
            jansi = true;
        }
        commandContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/logback.xml", logback.template(jansi)));
    }
}
