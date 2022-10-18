package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

class KoTestSpec extends ApplicationContextSpec {

    void 'test maven configure unit tests'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .testFramework(TestFramework.KOTEST)
                .render()

        then:
        template.contains('''
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <includes>
            <include>**/*Spec.*</include>
            <include>**/*Test.*</include>
          </includes>
        </configuration>
        <dependencies>
          <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit5.version}</version>
          </dependency>
        </dependencies>
      </plugin>
''')

    }
    void 'test maven configure unit tests for Kotest5'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .testFramework(TestFramework.KOTEST)
                .language(Language.KOTLIN)
                .render()

        then:
        template.contains('''
    <dependency>
      <groupId>io.kotest</groupId>
      <artifactId>kotest-assertions-core-jvm</artifactId>
      <scope>test</scope>
    </dependency>
''')
        template.contains('''
    <dependency>
      <groupId>io.kotest</groupId>
      <artifactId>kotest-runner-junit5-jvm</artifactId>
      <scope>test</scope>
    </dependency>
''')
        template.contains('''
    <dependency>
      <groupId>io.micronaut.test</groupId>
      <artifactId>micronaut-test-kotest5</artifactId>
      <scope>test</scope>
    </dependency>
''')
        template.contains('''
    <dependency>
      <groupId>io.mockk</groupId>
      <artifactId>mockk</artifactId>
      <scope>test</scope>
    </dependency>
''')

    }

    void 'test gradle configure unit tests for Kotest5'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .testFramework(TestFramework.KOTEST)
                .render()

        then:
        template.contains('testRuntime("kotest5")')
    }
}
