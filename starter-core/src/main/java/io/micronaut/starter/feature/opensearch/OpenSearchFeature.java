/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.opensearch;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import io.micronaut.testresources.buildtools.KnownModules;

import java.util.Collections;
import java.util.List;

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_TESTRESOURCES;

public abstract class OpenSearchFeature extends EaseTestingFeature implements OpenSearchContributingTestContainerDependency, TestResourcesAdditionalModulesProvider {

    protected OpenSearchFeature(
            TestContainers testContainers,
            TestResources testResources
    ) {
        super(testContainers, testResources);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-opensearch/latest/guide/";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://opensearch.org/docs/latest/clients/java/";
    }

    @Override
    public String getCategory() {
        return Category.SEARCH;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils
                .opensearchDependency()
                .artifactId("micronaut-" + getName())
                .compile());
    }

    @Override
    public List<String> getTestResourcesAdditionalModules(GeneratorContext generatorContext) {
        return Collections.singletonList(KnownModules.OPENSEARCH);
    }

    @Override
    public List<MavenCoordinate> getTestResourcesDependencies(GeneratorContext generatorContext) {
        return Collections.singletonList(new MavenCoordinate(GROUP_ID_MICRONAUT_TESTRESOURCES, "micronaut-test-resources-opensearch", null));
    }
}
