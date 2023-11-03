package io.micronaut.starter.feature.micrometer

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class MicrometerObservationHttpSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    MicrometerObservationHttp feature = beanContext.getBean(MicrometerObservationHttp)

    @Unroll
    void 'test micrometer feature micrometer-observation-http contributes dependencies for #buildTool'(BuildTool buildTool) {
        given:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["micrometer-observation-http"])
                .render()
        when:
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-observation-http", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-core", Scope.COMPILE)
        verifier.hasDependency("io.micronaut", "micronaut-management", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void 'test configuration for feature micrometer-observation-http'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(["micrometer-observation-http"])

        then: 'the configuration is enabled for the feature'
        commandContext.configuration.get('micrometer.observation.http.server.enabled') == true
        commandContext.configuration.get('micrometer.observation.http.client.enabled') == true
    }

    void "micrometer-observation-http belongs to Metrics category"() {
        expect:
        Category.METRICS == feature.category
    }

    void "micrometer-observation-http supports every application type"(ApplicationType applicationType) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values() - ApplicationType.DEFAULT
    }
}
