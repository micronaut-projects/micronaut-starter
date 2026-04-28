package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class JspecifySpec extends ApplicationContextSpec implements CommandOutputFixture{

    @Shared
    @Subject
    Jspecify jspecify = beanContext.getBean(Jspecify)

    void "jspecify supports application type #appType"(ApplicationType appType) {
        expect:
        jspecify.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "jspecify dependencies  #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['jspecify'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("jspecify")

        where:
        buildTool << BuildTool.values()
    }

    void 'test README.md with feature jspecify contains links to docs'() {
        when:
        def output = generate(['jspecify'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/#jspecify")
        readme.contains("https://jspecify.dev/docs/start-here/")
    }

}
