package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SimpleLoggingSpec extends ApplicationContextSpec  implements CommandOutputFixture {
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
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['slf4j-simple'])
                .language(language)
                .render()

        then:
        template.contains('runtimeOnly("org.slf4j:slf4j-simple")')
        !template.contains('runtimeOnly("ch.qos.logback:logback-classic")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test slf4j-simple feature for Maven and language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['slf4j-simple'])
                .render()

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
