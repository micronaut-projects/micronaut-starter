package io.micronaut.starter.feature.discovery

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class EurekaSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature discovery-eureka contains links to micronaut docs'() {
        when:
        def output = generate(['discovery-eureka'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#serviceDiscoveryEureka")
    }

    @Unroll
    void 'test gradle discovery-eureka feature for language=#language and buildTool=#buildTool'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['discovery-eureka'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.discovery", "micronaut-discovery-client", Scope.COMPILE)

        and: 'It does not define a dependency on micronaut-discovery-core since it is pulled transitively'
        !verifier.hasDependency("io.micronaut", "micronaut-discovery-core", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }

    void 'test discovery-eureka configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-eureka'])

        then:
        commandContext.configuration.get('eureka.client.registration.enabled'.toString()) == true
        commandContext.configuration.get('eureka.client.defaultZone') == '${EUREKA_HOST:localhost}:${EUREKA_PORT:8761}'
    }

}
