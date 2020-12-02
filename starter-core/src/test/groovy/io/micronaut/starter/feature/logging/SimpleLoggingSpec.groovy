package io.micronaut.starter.feature.logging

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom


class SimpleLoggingSpec extends BeanContextSpec  implements CommandOutputFixture {
    void 'test generate simple logger properties'() {
        when:
        def output = generate(['slf4j-simple'])
        def logging = output["src/main/resources/simplelogger.properties"]

        then:
        logging
        logging.contains("org.slf4j.simpleLogger.defaultLogLevel=info")
    }

    @Unroll
    void 'test configure slf4j-simple for language=#language'() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['slf4j-simple'], language), false).render().toString()

        then:
        template.contains('runtimeOnly("org.slf4j:slf4j-simple")')
        !template.contains('runtimeOnly("ch.qos.logback:logback-classic")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test slf4j-simple feature for Maven and language=#language'() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['slf4j-simple'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-simple</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        !template.contains("""
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <scope>runtime</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

}
