package io.micronaut.starter.feature.server;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.OneOfFeature;

public interface ServerFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return ServerFeature.class;
    }

    @Override
    default boolean supports(MicronautCommand command) {
        return command == MicronautCommand.CREATE_APP;
    }
}
