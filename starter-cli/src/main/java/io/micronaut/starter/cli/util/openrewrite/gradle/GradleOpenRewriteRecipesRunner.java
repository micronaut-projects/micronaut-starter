/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.cli.util.openrewrite.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.cli.util.openrewrite.ConsumerOutputStream;
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteConfiguration;
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteRecipesRunner;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;

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

        Path initScript = null;
        try {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("openrewrite/init.gradle")) {
                if (is == null) {
                    throw new IllegalStateException("Missing classpath resource: openrewrite/init.gradle");
                }
                initScript = Files.createTempFile("mn-openrewrite-init", ".gradle");
                Files.writeString(initScript, new String(is.readAllBytes(), StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            }

            Map<String, String> sysProps = systemProperties(configuration);
            List<String> args = new ArrayList<>();
            args.add("-Duser.dir=" + folder.getAbsolutePath());
            args.add("--init-script");
            args.add(initScript.toString());

            try (ProjectConnection connection = GradleConnector.newConnector()
                    .forProjectDirectory(folder)
                    .connect()) {
                connection.newBuild()
                        .forTasks(TASK_REWRITE_RUN)
                        .withArguments(args.toArray(new String[0]))
                        .withSystemProperties(sysProps)
                        .setStandardOutput(new ConsumerOutputStream(out))
                        .setStandardError(new ConsumerOutputStream(err))
                        .run();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (initScript != null) {
                try {
                    Files.deleteIfExists(initScript);
                } catch (IOException ignored) {

                }
            }
        }
    }

    private Map<String, String> systemProperties(OpenRewriteConfiguration configuration) {
        Map<String, String> systemProperties = new HashMap<>();
        Map<String, Object> configurationSystemProperties = configuration.getSystemProperties();
        for (String k : configurationSystemProperties.keySet()) {
            Object value = configurationSystemProperties.get(k);
            if (value != null) {
                systemProperties.put(k, value.toString());
            }

        }
        return systemProperties;
    }
}
