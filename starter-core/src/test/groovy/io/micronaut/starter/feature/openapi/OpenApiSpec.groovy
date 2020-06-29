package io.micronaut.starter.feature.openapi

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class OpenApiSpec extends BeanContextSpec  implements CommandOutputFixture {
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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], language)).render().toString()

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
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], Language.JAVA), []).render().toString()

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
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], Language.KOTLIN), []).render().toString()

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
        template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['openapi'], Language.GROOVY), []).render().toString()

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

    @Requires({ jvm.isJava8() || jvm.isJava11() })
    void 'test resource-config is generated with graalvm feature'() {
        when:
        def output = generate(['openapi', 'graalvm'])
        def resourceConfigJson = output["src/main/resources/META-INF/native-image/example.micronaut/foo-application/resource-config.json"]

        then:
        resourceConfigJson.contains('{"pattern":"\\\\QMETA-INF/swagger\\\\E"}')
        resourceConfigJson.contains('{"pattern":".*/swagger/foo-.*yml"}')
        resourceConfigJson.contains('{"pattern":"\\\\QMETA-INF/swagger/views/rapidoc/index.html\\\\E"}')
        resourceConfigJson.contains('{"pattern":"\\\\QMETA-INF/swagger/views/redoc/index.html\\\\E"}')
        resourceConfigJson.contains('{"pattern":"\\\\QMETA-INF/swagger/views/swagger-ui/index.html\\\\E"}')
    }

    void 'test resource-config is not generated without graalvm feature'() {
        when:
        def output = generate(['openapi'])
        def resourceConfigJson = output["src/main/resources/META-INF/native-image/example.micronaut/foo-application/resource-config.json"]

        then:
        !resourceConfigJson
    }
}
