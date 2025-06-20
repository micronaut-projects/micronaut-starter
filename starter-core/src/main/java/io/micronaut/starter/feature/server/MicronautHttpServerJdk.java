/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.starter.feature.server;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Singleton
public class MicronautHttpServerJdk extends AbstractMicronautServerFeature {
    private static final String ARTIFACT_ID_MICRONAUT_HTTP_SERVER_JDK = "micronaut-http-server-jdk";
    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_SERVER_JDK = MicronautDependencyUtils.servletDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_SERVER_JDK)
            .compile()
            .build();

    @Override
    public String getName() {
        return "http-server-jdk";
    }

    @Override
    public String getTitle() {
        return "Built-In Java HTTP Server Runtime";
    }

    @Override
    public String getDescription() {
        return "Add a server runtime based on the Java built-in Http Server";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-servlet/latest/guide/#httpServer";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_HTTP_SERVER_JDK);
        }
    }

    @Override
    public String resolveMicronautRuntime(GeneratorContext generatorContext) {
        return "http_server_jdk";
    }
}
