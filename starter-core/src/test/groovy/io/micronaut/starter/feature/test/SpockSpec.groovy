package io.micronaut.starter.feature.test

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.TestFramework

class SpockSpec extends BeanContextSpec {

    void 'test maven configure unit tests'() {
        given:
        Features features = getFeatures([], null, TestFramework.SPOCK, BuildTool.MAVEN)

        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

        then:
        template.contains("""
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <includes>
            <include>**/*Spec.*</include>
            <include>**/*Test.*</include>
          </includes>
        </configuration>
      </plugin>
""")

    }
}
