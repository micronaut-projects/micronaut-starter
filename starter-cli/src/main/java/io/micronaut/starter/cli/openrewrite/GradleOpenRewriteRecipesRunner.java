package io.micronaut.starter.cli.openrewrite;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.openrewrite.OpenRewriteConfiguration;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.build.BuildEnvironment;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Named("gradle")
@Singleton
public class GradleOpenRewriteRecipesRunner implements OpenRewriteRecipesRunner {

    public static final String TASK_REWRITE_RUN = "rewriteRun";

    @Override
    public void run(@NonNull List<String> recipes,
                    @NonNull File folder,
                    @NonNull OpenRewriteConfiguration configuration,
                    @NonNull Consumer<String> out,
                    @NonNull Consumer<String> err) {

        try (ProjectConnection connection = GradleConnector.newConnector()
                .forProjectDirectory(folder)
                .connect()) {
            connection.newBuild()
                    .forTasks(TASK_REWRITE_RUN)
                    .withSystemProperties(systemProperties(configuration))
                    .setStandardOutput(new ConsumerOutputStream(out))
                    .setStandardError(new ConsumerOutputStream(err))
                    .run();
        }
    }

    private Map<String, String> systemProperties(OpenRewriteConfiguration configuration) {
        Map<String, String> systemProperties = new HashMap<>();
        Map<String, Object> configurationSystemProperties = configuration.getSystemProperties();
        for (String k : configurationSystemProperties.keySet()) {
            systemProperties.put(k, configurationSystemProperties.get(k).toString());
        }
        return systemProperties;
    }
}
