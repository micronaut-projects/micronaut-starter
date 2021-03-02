package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.build.maven.MavenBuild
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class OpenApiSpec extends ApplicationContextSpec  implements CommandOutputFixture {

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
        Category.API == beanContext.getBean(OpenApi).category
    }

    @Unroll
    void 'test swagger with Gradle for language=#language'() {
        given:
        Project project = buildProject()
        BuildTool buildTool = BuildTool.GRADLE
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['openapi'], language)
        Options options = new Options(language, buildTool)
        GradleBuild gBuild = gradleBuild(options, features, project, type)
        String template = buildGradle.template(type, project, features, gBuild).render().toString()

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
        given:
        Language language = Language.JAVA
        Project project = buildProject()
        BuildTool buildTool = BuildTool.MAVEN
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures(['openapi'], language)
        Options options = new Options(language, buildTool)
        MavenBuild mvnBuild = mavenBuild(options, features, project, type)
        String template = pom.template(type, project, features, mvnBuild).render().toString()
        then:
        template.contains("""
    <dependency>
      <groupId>io.swagger.core.v3</groupId>
      <artifactId>swagger-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("<micronaut.openapi.version>")
        template.contains("</micronaut.openapi.version>")
        template.contains('''
            <path>
              <groupId>io.micronaut.openapi</groupId>
              <artifactId>micronaut-openapi</artifactId>
              <version>${micronaut.openapi.version}</version>
            </path>
''')

        when:
        features = getFeatures(['openapi'], Language.KOTLIN)
        options = new Options(language, buildTool)
        mvnBuild = mavenBuild(options, features, project, type)
        template = pom.template(type, project, features, mvnBuild).render().toString()

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
        features = getFeatures(['openapi'], Language.GROOVY)
        options = new Options(language, buildTool)
        mvnBuild = mavenBuild(options, features, project, type)
        template = pom.template(type, project, features, mvnBuild).render().toString()

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
