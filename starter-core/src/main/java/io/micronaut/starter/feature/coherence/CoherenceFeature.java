/*
 * Copyright 2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.coherence;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;

/**
 * Base coherence feature.
 *
 * @author Pavol Gressa
 * @since 2.4
 */
@Singleton
public class CoherenceFeature implements Feature {
    public static final String COHERENCE_VERSION = "21.06-M1";

    @Override
    public String getName() {
        return "coherence";
    }

    @Override
    public String getTitle() {
        return "Coherence";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Coherence";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://coherence.java.net/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-coherence/1.0.x/guide/index.html";
    }

    static String getCoherenceVersionPropertyName(BuildTool buildTool) {
        if (buildTool.equals(BuildTool.MAVEN)) {
            return "coherence.version";
        } else {
            return "coherenceVersion";
        }
    }

    static String getCoherenceVersionProperty(BuildTool buildTool) {
        return "${" + getCoherenceVersionPropertyName(buildTool) + "}";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getBuildProperties().put(
                getCoherenceVersionPropertyName(generatorContext.getBuildTool()), COHERENCE_VERSION);


        Dependency.Builder coherenceMicronaut = Dependency.builder()
                .groupId("io.micronaut.coherence")
                .artifactId("micronaut-coherence")
                .template();
        Dependency.Builder coherence = Dependency.builder()
                .groupId("com.oracle.coherence.ce")
                .artifactId("coherence")
                .version(getCoherenceVersionProperty(generatorContext.getBuildTool()))
                .template();

        generatorContext.addDependency(coherence.compile());
        generatorContext.addDependency(coherenceMicronaut.compile());
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
