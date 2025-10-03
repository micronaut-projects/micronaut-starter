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
import io.micronaut.starter.application.OperatingSystem;
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteConfiguration;
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteRecipesRunner;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;

import java.io.File;
import java.util.List;
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


            List<String> cmd = new ArrayList<>();
            boolean isWindows = configuration.operatingSystem() == OperatingSystem.WINDOWS;
            File wrapper = new File(folder, isWindows ? "gradlew.bat" : "gradlew");
            if (wrapper.isFile()) {
                if (isWindows) {
                    cmd.add("cmd");
                    cmd.add("/c");
                    cmd.add(wrapper.getAbsolutePath());
                } else {
                    wrapper.setExecutable(true);
                    cmd.add(wrapper.getAbsolutePath());
                }
            } else {
                if (isWindows) {
                    cmd.add("cmd");
                    cmd.add("/c");
                    cmd.add("gradle.bat");
                } else {
                    cmd.add("gradle");
                }
            }
            cmd.add("--init-script");
            cmd.add(initScript.toString());
            cmd.addAll(configuration.getSystemPropertiesList());
            cmd.add(TASK_REWRITE_RUN);

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(folder);
            Process process = pb.start();

            Thread outPump = new Thread(() -> pump(process.getInputStream(), out));
            Thread errPump = new Thread(() -> pump(process.getErrorStream(), err));
            outPump.start();
            errPump.start();

            int exit;
            try {
                exit = process.waitFor();
                outPump.join();
                errPump.join();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ie);
            }
            if (exit != 0) {
                throw new IllegalStateException("Gradle exited with code " + exit + " while running " + TASK_REWRITE_RUN);
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


    private void pump(InputStream in, Consumer<String> consumer) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException ioe) {
            consumer.accept(ioe.getMessage() != null ? ioe.getMessage() : ioe.toString());
        }
    }
}
