package io.micronaut.starter.feature.dependencies

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

import java.util.regex.Pattern

class DependenciesFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Override
    Map<String, Object> getConfiguration() {
        ['spec.name': 'DependenciesFeatureSpec']
    }

    @Unroll
    void 'test gradle geb feature for language=#language and spock'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['geb'])
                .language(language)
                .testFramework(TestFramework.SPOCK)
                .render()

        then:
        template.contains('testImplementation("org.gebish:geb-spock:4.0")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-firefox-driver:3.141.59")')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-support:3.141.59")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle geb feature for language=#language and junit'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['geb'])
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        template.contains('testImplementation("org.gebish:geb-junit5:')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-firefox-driver:')
        template.contains('testRuntimeOnly("org.seleniumhq.selenium:selenium-support:')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test gradle mybatis feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['mybatis'])
                .language(language)
                .render()

        then:
        template.contains('implementation("org.mybatis:mybatis:')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven mybatis feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['mybatis'])
                .render()

        then:
        Pattern.compile("""
    <dependency>
      <groupId>org\\.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>.*?</version>
      <scope>compile</scope>
    </dependency>
""").matcher(template).find()

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'test maven geb feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['geb'])
                .render()

        then:
        if (language == Language.GROOVY) {
            assert Pattern.compile("""
    <dependency>
      <groupId>org\\.gebish</groupId>
      <artifactId>geb-spock</artifactId>
      <version>.*?</version>
      <scope>test</scope>
    </dependency>
""").matcher(template).find()
        } else {
            assert Pattern.compile("""
    <dependency>
      <groupId>org\\.gebish</groupId>
      <artifactId>geb-junit5</artifactId>
      <version>.*?</version>
      <scope>test</scope>
    </dependency>
""").matcher(template).find()
        }

        and:
        Pattern.compile("""
    <dependency>
      <groupId>org\\.seleniumhq\\.selenium</groupId>
      <artifactId>selenium-firefox-driver</artifactId>
      <version>.*?</version>
      <scope>test</scope>
    </dependency>
""").matcher(template).find()
        and:
        Pattern.compile("""
    <dependency>
      <groupId>org\\.seleniumhq\\.selenium</groupId>
      <artifactId>selenium-support</artifactId>
      <version>.*?</version>
      <scope>test</scope>
    </dependency>
""").matcher(template).find()

        where:
        language << Language.values().toList()
    }
}
