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
package io.micronaut.starter.feature.test;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import jakarta.inject.Singleton;

@Singleton
public class Spock implements TestFeature {
    protected static final String GROUP_ID_SPOCKFRAMEWORK = "org.spockframework";
    protected static final String ARTIFACT_ID_SPOCK_CORE = "spock-core";
    protected static final String GROUP_ID_CODEHAUS_GROOVY = "org.codehaus.groovy";
    protected static final String ARTIFACT_ID_GROOVY_ALL = "groovy-all";
    protected static final Dependency DEPENDENCY_MICRONAUT_INJECT_GROOVY = MicronautDependencyUtils
            .coreDependency()
            .artifactId("micronaut-inject-groovy")
            .test()
            .build();

    protected static final Dependency DEPENDENCY_MICRONAUT_TEST_SPOCK = MicronautDependencyUtils
            .testDependency()
            .artifactId("micronaut-test-spock")
            .test()
            .build();

    protected static final Dependency DEPENDENCY_SPOCK_CORE_EXCLUDING_GROOVY_ALL = Dependency.builder()
            .groupId(GROUP_ID_SPOCKFRAMEWORK)
            .artifactId(ARTIFACT_ID_SPOCK_CORE)
            .exclude(Dependency.builder()
                    .groupId(GROUP_ID_CODEHAUS_GROOVY)
                    .artifactId(ARTIFACT_ID_GROOVY_ALL)
                    .build())
            .test()
            .build();

    @Override
    @NonNull
    public String getName() {
        return "spock";
    }

    @Override
    public void doApply(GeneratorContext generatorContext) {
        // Only for Maven, these dependencies are applied by the Micronaut Gradle Plugin
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            if (generatorContext.getLanguage() != Language.GROOVY) {
                generatorContext.addDependency(DEPENDENCY_MICRONAUT_INJECT_GROOVY);
            }
            generatorContext.addDependency(DEPENDENCY_SPOCK_CORE_EXCLUDING_GROOVY_ALL);
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_TEST_SPOCK);
        }
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.SPOCK;
    }

}
