/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.feature.elasticsearch;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.graalvm.GraalVM;

import javax.inject.Singleton;

@Singleton
public class Elasticsearch implements Feature {

    @Override
    public String getName() {
        return "elasticsearch";
    }

    @Override
    public String getTitle() {
        return "Elasticsearch Driver";
    }

    @Override
    public String getDescription() {
        return "Adds support for Elasticsearch in the application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("elasticsearch.httpHosts", "http://localhost:9200,http://127.0.0.2:9200");
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.elasticsearch")
                .artifactId("micronaut-elasticsearch")
                .compile());
        if (generatorContext.isFeaturePresent(GraalVM.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.slf4j")
                    .lookupArtifactId("log4j-over-slf4j")
                    .runtime());
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.apache.logging.log4j")
                    .lookupArtifactId("log4j-api")
                    .compile());
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.apache.logging.log4j")
                    .lookupArtifactId("log4j-core")
                    .compile());
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.SEARCH;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-elasticsearch/latest/guide/index.html";
    }
}
