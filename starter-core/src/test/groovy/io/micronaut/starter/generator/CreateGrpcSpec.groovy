package io.micronaut.starter.generator

import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateGrpcSpec extends CommandSpec {

    @Unroll
    void 'test basic create-grpc-app for lang=#lang'() {
        given:
        generateGrpcProject(lang)

        when:
        executeGradleCommand('run')

        then:
        testOutputContains("Startup completed")

        where:
        lang << [Language.JAVA, Language.GROOVY, Language.KOTLIN, null]
    }

    @Unroll
    void 'test basic create-grpc-app for lang=#lang and maven'() {
        given:
        generateGrpcProject(lang, BuildTool.MAVEN)

        when:
        executeMavenCommand("mn:run")

        then:
        testOutputContains("Startup completed")

        where:
        lang << [Language.JAVA, Language.GROOVY, Language.KOTLIN, null]
    }

}
