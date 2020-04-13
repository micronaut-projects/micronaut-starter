package io.micronaut.starter.feature.build.maven

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class MavenSpec extends Specification implements ProjectFixture, ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "test annotation processor dependencies"() {
        when:
        Features features = getFeatures([], null, null, BuildTool.maven)
        String template = pom.template(buildProject(), features, []).render().toString()

        then:
        template.contains("""
          <annotationProcessorPaths>
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-inject-java</artifactId>
              <version>\${micronaut.version}</version>
            </path>
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-validation</artifactId>
              <version>\${micronaut.version}</version>
            </path>
          </annotationProcessorPaths>
""")

        when:
        features = getFeatures([], Language.kotlin, null, BuildTool.maven)
        template = pom.template(buildProject(), features, []).render().toString()

        then:
        template.contains("""
              <annotationProcessorPaths>
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-inject-java</artifactId>
                  <version>\${micronaut.version}</version>
                </annotationProcessorPath>
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-validation</artifactId>
                  <version>\${micronaut.version}</version>
                </annotationProcessorPath>
              </annotationProcessorPaths>
""")

        when:
        features = getFeatures([], Language.groovy, null, BuildTool.maven)
        template = pom.template(buildProject(), features, []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-inject</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-validation</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }

}
