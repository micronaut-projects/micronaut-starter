package io.micronaut.starter.feature.validation;

import io.micronaut.starter.application.generator.GeneratorContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface ConfigurationBlockProvider {
    @Nullable ConfigurationValidationBlock configurationValidation(@NonNull GeneratorContext generatorContext);
}
