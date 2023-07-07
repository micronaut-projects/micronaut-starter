/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.build.maven;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Singleton
public class GroovyMavenPlugin implements Feature {

    private static final String GROUP_ID_GMAVEN = "org.codehaus.gmaven";
    private static final String ARTIFACT_ID_GMAVEN = "gmavenplus-plugin";
    @Override
    public String getName() {
        return "groovy-maven-plugin";
    }

    @Override
    public String getTitle() {
        return "Groovy Maven Plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .groupId(GROUP_ID_GMAVEN)
                    .artifactId(ARTIFACT_ID_GMAVEN)
                    .build());
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://groovy.github.io/gmaven/groovy-maven-plugin/index.html";
    }
}
