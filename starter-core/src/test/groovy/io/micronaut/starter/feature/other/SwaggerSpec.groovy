package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class SwaggerSpec extends BeanContextSpec {

    @Unroll
    void 'test swagger with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['swagger'], language)).render().toString()

        then:
        template.contains('implementation("io.swagger.core.v3:swagger-annotations")')
        template.contains("$scope(\"io.micronaut.configuration:micronaut-openapi\")")

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven swagger feature'() {
        when:
        String template = pom.template(buildProject(), getFeatures(['swagger'], Language.JAVA), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
            <path>
              <groupId>io.micronaut.configuration</groupId>
              <artifactId>micronaut-openapi</artifactId>
              <version>\${micronaut.openapi.version}</version>
            </path>
""")

        when:
        template = pom.template(buildProject(), getFeatures(['swagger'], Language.KOTLIN), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
                <annotationProcessorPath>
                    <groupId>io.micronaut.configuration</groupId>
                    <artifactId>micronaut-openapi</artifactId>
                    <version>\${micronaut.openapi.version}</version>
                </annotationProcessorPath>
""")

        when:
        template = pom.template(buildProject(), getFeatures(['swagger'], Language.GROOVY), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-openapi</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}
