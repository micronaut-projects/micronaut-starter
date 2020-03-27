package io.micronaut.starter.feature.jdbc;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.OneOfFeature;

import java.util.Collections;
import java.util.HashMap;

public interface JdbcFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return JdbcFeature.class;
    }

    @Override
    default void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("datasources.default", Collections.emptyMap());
    }
}
