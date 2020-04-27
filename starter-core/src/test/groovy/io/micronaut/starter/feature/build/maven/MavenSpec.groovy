package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class MavenSpec extends BeanContextSpec {

    void "test annotation processor dependencies"() {
        when:
        Features features = getFeatures([], null, null, BuildTool.MAVEN)
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

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
        features = getFeatures([], Language.KOTLIN, null, BuildTool.MAVEN)
        template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

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
        features = getFeatures([], Language.GROOVY, null, BuildTool.MAVEN)
        template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

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
