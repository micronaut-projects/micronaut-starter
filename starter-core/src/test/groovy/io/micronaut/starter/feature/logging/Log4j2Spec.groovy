package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

import java.util.regex.Pattern

class Log4j2Spec extends ApplicationContextSpec {

    @Unroll
    void "test dependency added for lg4j2 feature" (BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Log4j2.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasBom("org.apache.logging.log4j", "log4j-bom", Scope.COMPILE)
        verifier.hasDependency("org.apache.logging.log4j", "log4j-api", Scope.COMPILE)
        verifier.hasDependency("org.apache.logging.log4j", "log4j-core", Scope.RUNTIME)
        verifier.hasDependency("org.apache.logging.log4j", "log4j-slf4j-impl", Scope.RUNTIME)

        where:
        buildTool << BuildTool.values()

    }
}
