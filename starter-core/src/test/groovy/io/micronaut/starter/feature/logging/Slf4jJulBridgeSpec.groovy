package io.micronaut.starter.feature.logging

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class Slf4jJulBridgeSpec  extends ApplicationContextSpec  implements CommandOutputFixture {

    private static final JUL_FEATURE = "jul-to-slf4j"

    void "logback configuration is generated with JUL config"() {
        when:
        def output = generate([JUL_FEATURE])
        String xml = output["src/main/resources/logback.xml"]

        then:
        xml
        xml.contains("<contextListener class=\"ch.qos.logback.classic.jul.LevelChangePropagator\"/>")
        !xml.contains("<withJansi>true</withJansi>")
        xml.contains("<pattern>%cyan(%d{HH:mm:ss.SSS}) %gray([%thread]) %highlight(%-5level) %magenta(%logger{36}) - %msg%n</pattern>")
        !xml.contains("<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>")
    }

    void "JUL logging.properties file is generated with SLF4JBridgeHandler"() {
        when:
        def output = generate([JUL_FEATURE])
        def props = output["src/main/resources/logging.properties"]

        then:
        props
        props.contains("handlers = org.slf4j.bridge.SLF4JBridgeHandler")
    }

    void "logback and jul-to-slf4j dependencies are configured for gradle" (BuildTool buildTool)  {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([JUL_FEATURE])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("ch.qos.logback", "logback-classic", Scope.RUNTIME)
        verifier.hasDependency("org.slf4j", "jul-to-slf4j", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }
}
