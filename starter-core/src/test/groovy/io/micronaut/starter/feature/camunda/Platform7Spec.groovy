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
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class Platform7Spec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature camunda-platform7 contains links to micronaut docs'() {
        when:
        def output = generate(['camunda-platform7'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://assertj.github.io/doc/")
        readme.contains("https://github.com/camunda-community-hub/micronaut-camunda-bpm")
    }

    @Unroll
    void 'test gradle camunda-platform7 feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['camunda-platform7'])
                .render()

        then:
        template.count('implementation("info.novatec:micronaut-camunda-bpm-feature:') == 1
        template.count('testImplementation("org.assertj:assertj-core")') == 1
        template.count('testImplementation("org.camunda.bpm:camunda-bpm-assert') == 1
        template.count('runtimeOnly("com.h2database:h2")') == 1

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven camunda-platform7 feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['camunda-platform7'])
                .render()

        then:
        template.count('''\
    <dependency>
      <groupId>info.novatec</groupId>
      <artifactId>micronaut-camunda-bpm-feature</artifactId>
 ''') == 1
        template.count('''\
    <dependency>
      <groupId>org.camunda.bpm</groupId>
      <artifactId>camunda-bpm-assert</artifactId>
 ''') == 1
        template.count('''\
    <dependency>
      <groupId>info.novatec</groupId>
      <artifactId>micronaut-camunda-bpm-feature</artifactId>
 ''') == 1
        template.count('''\
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
''') == 1
        where:
        language << Language.values().toList()
    }
}
