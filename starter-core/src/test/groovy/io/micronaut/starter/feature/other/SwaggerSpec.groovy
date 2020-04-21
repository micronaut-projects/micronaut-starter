package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.options.Language
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
class SwaggerSpec extends BeanContextSpec {
    @Unroll
    void 'test swagger with Gradle for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['swagger'], language)).render().toString()

        then:
        template.contains('implementation "io.swagger.core.v3:swagger-annotations"')
        template.contains("$scope \"io.micronaut.configuration:micronaut-openapi\"")


        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    @Unroll
    void 'test swagger with Maven for language=#language'() {
        when:
        String template = pom.template(buildProject(), getFeatures(["swagger"]), []).render().toString()

        then:
        template.contains('<artifactId>swagger-annotations</artifactId>')
        template.contains("<artifactId>micronaut-openapi</artifactId>")


        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }
}
