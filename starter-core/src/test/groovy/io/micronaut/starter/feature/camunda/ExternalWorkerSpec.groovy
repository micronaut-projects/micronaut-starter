/*
 * Copyright 2020 original authors
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
package io.micronaut.starter.feature.camunda

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.CommunityFeatureValidator
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Requires

class ExternalWorkerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Requires({ CommunityFeatureValidator.ENABLE_COMMUNITY_FEATURES })
    void 'test readme.md with feature camunda-external-worker contains links to micronaut docs'() {
        when:
        def output = generate(['camunda-external-worker'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/camunda-community-hub/micronaut-camunda-external-client")
    }

    @Requires({ CommunityFeatureValidator.ENABLE_COMMUNITY_FEATURES })
    void "test dependency added for camunda-external-worker feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([ExternalWorker.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("info.novatec", "micronaut-camunda-external-client-feature", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    @Requires({ CommunityFeatureValidator.ENABLE_COMMUNITY_FEATURES })
    void 'test camunda-external-worker configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['camunda-external-worker'])

        then:
        commandContext.configuration.get('camunda.external-client.base-url'.toString()) == "http://localhost:8080/engine-rest"
    }
}
