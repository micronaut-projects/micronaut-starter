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
package io.micronaut.starter.feature.spring

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class SpringTxSpec extends BeanContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    SpringTx springTx = beanContext.getBean(SpringTx)

    void 'spring-tx belongs to Spring category'() {
        expect:
        Category.SPRING == springTx.category
    }

    void 'test readme.md with feature spring-tx contains links to micronaut docs'() {
        when:
        def output = generate(['spring-tx'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#_using_spring_transaction_management")
    }

    void 'spring-tx is visible'() {
        expect:
        springTx.visible
    }

    void 'spring-tx title and description are different'() {
        expect:
        springTx.getTitle()
        springTx.getDescription()
        springTx.getTitle() != springTx.getDescription()
    }

    @Unroll
    void 'feature spring-tx supports every type of application type. applicationType=#applicationType'() {
        expect:
        springTx.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    void 'spring-tx does not include spring feature'() {
        when:
        Features features = getFeatures(['spring-tx'])

        then:
        features.contains('spring-tx')
        !features.contains('spring')
    }

    @Unroll
    void 'spring-tx with Gradle for language=#language adds dependency io.micronaut.sql:micronaut-hibernate-jpa-spring'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-tx'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-hibernate-jpa-spring")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'spring-tx with Maven feature for language=#language adds dependency io.micronaut.sql:micronaut-hibernate-jpa-spring'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['spring-tx'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-hibernate-jpa-spring</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

}
