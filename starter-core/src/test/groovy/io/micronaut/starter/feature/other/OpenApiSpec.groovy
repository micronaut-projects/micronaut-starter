package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class OpenApiSpec extends ApplicationContextSpec  implements CommandOutputFixture {
    @Shared
    @Subject
    OpenApi openApi = beanContext.getBean(OpenApi)

    void 'test readme.md with feature openapi contains links to micronaut docs'() {
        when:
        def output = generate(['openapi'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.openapis.org")
        readme.contains("https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html")
    }

    void "openApi belongs to API category"() {
        expect:
        Category.API == openApi.category
    }

    @Unroll
    void 'test swagger with Gradle for language=#language'() {
        when:
        def dependencies = getFeatureDependencies(OpenApi, BuildTool.GRADLE, TestFramework.JUNIT)
        def annotationProcessors = getAnnotationProcessors(OpenApi, BuildTool.GRADLE, TestFramework.JUNIT)

        then:
        dependencies

        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], language), false, dependencies, annotationProcessors).render().toString()

        then:
        template.contains('implementation("io.swagger.core.v3:swagger-annotations")')
        template.contains("$scope(\"io.micronaut.openapi:micronaut-openapi\")")

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven swagger feature'() {
        when:
        def dependencies = getFeatureDependencies(OpenApi, BuildTool.MAVEN, TestFramework.JUNIT)
        def annotationProcessors = getAnnotationProcessors(OpenApi, BuildTool.MAVEN, TestFramework.JUNIT)

        then:
        dependencies
        annotationProcessors

        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], Language.JAVA), [], dependencies, annotationProcessors).render().toString()

        println template
        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains('''
            <path>
              <groupId>io.micronaut.openapi</groupId>
              <artifactId>micronaut-openapi</artifactId>
              <version>${micronaut.openapi.version}</version>
            </path>
''')

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], Language.KOTLIN), [], dependencies, annotationProcessors).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains('''
                <annotationProcessorPath>
                  <groupId>io.micronaut.openapi</groupId>
                  <artifactId>micronaut-openapi</artifactId>
                  <version>${micronaut.openapi.version}</version>
                </annotationProcessorPath>
''')

        when:
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], Language.GROOVY), [], dependencies, annotationProcessors).render().toString()

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
      <groupId>io.micronaut.openapi</groupId>
      <artifactId>micronaut-openapi</artifactId>
      <scope>compile</scope>
    </dependency>
""")
    }
}
