package io.micronaut.starter.feature.test

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.TestFramework

class KoTestSpec extends BeanContextSpec {

    void 'test maven configure unit tests'() {
        given:
        Features features = getFeatures([], null, TestFramework.KOTEST, BuildTool.MAVEN)

        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, [], []).render().toString()

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
