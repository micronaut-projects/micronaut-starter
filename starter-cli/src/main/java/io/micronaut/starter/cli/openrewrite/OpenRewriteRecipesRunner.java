package io.micronaut.starter.cli.openrewrite;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.openrewrite.OpenRewriteConfiguration;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public interface OpenRewriteRecipesRunner {
    void run(@NonNull List<String> recipes,
             @NonNull File folder,
             @NonNull OpenRewriteConfiguration configuration,
             @NonNull Consumer<String> out,
             @NonNull Consumer<String> err);
}
