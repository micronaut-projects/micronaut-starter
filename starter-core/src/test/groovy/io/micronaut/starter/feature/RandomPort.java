package io.micronaut.starter.feature;

import io.micronaut.starter.Options;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class RandomPort implements DefaultFeature {

    @Override
    public String getName() {
        return "random-port";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Options options, List<Feature> selectedFeatures) {
        return micronautCommand == MicronautCommand.CREATE_APP;
    }

    @Override
    public String getTitle() {
        return "Random Port Support";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("micronaut.server.port", "-1");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
