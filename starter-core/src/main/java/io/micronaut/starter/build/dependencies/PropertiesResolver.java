package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Optional;

public interface PropertiesResolver {

    Optional<String> resolve(@NonNull String key);
}
