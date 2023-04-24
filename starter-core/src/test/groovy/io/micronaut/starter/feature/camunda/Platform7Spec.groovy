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

@Requires({ CommunityFeatureValidator.ENABLE_COMMUNITY_FEATURES })
class Platform7Spec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature camunda-platform7 contains links to micronaut docs'() {
        when:
        def output = generate(['camunda-platform7'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://assertj.github.io/doc/")
        readme.contains("https://github.com/camunda-community-hub/micronaut-camunda-platform-7")
        readme.contains("https://micronaut-projects.github.io/micronaut-servlet/1.0.x/guide/index.html#jetty")
    }

    void "test dependency added for camunda-platform7 feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Platform7.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("info.novatec", "micronaut-camunda-bpm-feature", Scope.COMPILE)
        verifier.hasDependency("org.camunda.bpm", "camunda-bpm-assert", Scope.TEST)
        verifier.hasDependency("org.assertj", "assertj-core", Scope.TEST)
        verifier.hasDependency("com.h2database", "h2", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()
    }

    void 'test camunda-platform7 configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['camunda-platform7'])

        then:
        commandContext.configuration.get('camunda.admin-user.id'.toString()) == "admin"
        commandContext.configuration.get('camunda.admin-user.password'.toString()) == "admin"
        commandContext.configuration.get('camunda.webapps.enabled') == true
        commandContext.configuration.get('camunda.rest.enabled') == true
        commandContext.configuration.get('camunda.generic-properties.properties.initialize-telemetry') == true
        commandContext.configuration.get('camunda.filter.create') == "All tasks"
    }
}
