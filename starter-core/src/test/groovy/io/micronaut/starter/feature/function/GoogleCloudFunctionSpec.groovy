package io.micronaut.starter.feature.function

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.VersionInfo
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class GoogleCloudFunctionSpec extends BeanContextSpec {

    @Unroll
    void 'test gradle google cloud function feature for language=#language'() {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['google-cloud-function'], language)).render().toString()

        then:
        template.contains('compileOnly "com.google.cloud.functions:functions-framework-api"')
        template.contains('invoker "com.google.cloud.functions.invoker:java-function-invoker:'+ VersionInfo.getDependencyVersion("google.function.invoker").getValue() +'"')
        !template.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !template.contains('implementation "io.micronaut:micronaut-http-client"')

        where:
        language << [Language.JAVA, Language.KOTLIN, Language.GROOVY]
    }
}
