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
package io.micronaut.starter.cli.util.openrewrite.maven;

import io.micronaut.starter.cli.util.openrewrite.OpenRewriteConfiguration;
import io.micronaut.starter.cli.util.openrewrite.OpenRewriteRecipesRunner;
import io.micronaut.starter.application.OperatingSystem;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Named("maven")
@Singleton
public class MavenOpenRewriteRecipesRunner implements OpenRewriteRecipesRunner {

    @Override
    public void run(List<String> recipes,
                    File folder,
                    OpenRewriteConfiguration configuration,
                    Consumer<String> out,
                    Consumer<String> err) {
        InvocationRequest request = createInvocationRequest(folder, configuration, out, err);
        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            throw new RuntimeException(e);
        }
    }

    private InvocationRequest createInvocationRequest(File folder,
                                                      OpenRewriteConfiguration configuration,
                                                      Consumer<String> out,
                                                      Consumer<String> err) {
        String goal = "rewrite:run";
        List<String> args = new ArrayList<>();
        args.add(goal);
        args.addAll(configuration.getSystemPropertiesList());

        File mavenWrapper = configuration.operatingSystem() == OperatingSystem.WINDOWS
                ? new File(folder, "mvnw.bat")
                : new File(folder, "mvnw");

        InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(folder);
        request.setPomFile(new File(folder, "pom.xml"));
        request.addArgs(args);
        request.setDebug(true);
        request.setOutputHandler(out::accept);
        request.setErrorHandler(err::accept);
        request.setBatchMode(true);
        request.setMavenExecutable(mavenWrapper.getAbsoluteFile());
        return request;
    }
}
