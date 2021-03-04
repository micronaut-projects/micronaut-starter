package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
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
}
