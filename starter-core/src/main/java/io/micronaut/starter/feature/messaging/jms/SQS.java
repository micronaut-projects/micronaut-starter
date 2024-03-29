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
package io.micronaut.starter.feature.messaging.jms;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import io.micronaut.testresources.buildtools.KnownModules;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_TESTRESOURCES;

@Singleton
public class SQS extends AbstractJmsFeature implements TestResourcesAdditionalModulesProvider {

    public static final String NAME = "jms-sqs";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "AWS SQS JMS Messaging";
    }

    @Override
    public String getDescription() {
        return "Adds support for AWS SQS JMS messaging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.jms.sqs.enabled", true);
        generatorContext.addDependency(MicronautDependencyUtils.jmsDependency()
                .artifactId("micronaut-jms-sqs")
                .compile());
    }

    @Override
    public List<String> getTestResourcesAdditionalModules(GeneratorContext generatorContext) {
        return Collections.singletonList(KnownModules.LOCALSTACK_SQS);
    }

    @Override
    public List<MavenCoordinate> getTestResourcesDependencies(GeneratorContext generatorContext) {
        return Collections.singletonList(new MavenCoordinate(GROUP_ID_MICRONAUT_TESTRESOURCES, "micronaut-test-resources-localstack-sqs", null));
    }
}
