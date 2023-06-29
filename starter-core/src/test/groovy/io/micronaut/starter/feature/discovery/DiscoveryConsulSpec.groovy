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
import spock.lang.Issue
import spock.lang.Unroll

class DiscoveryConsulSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature discovery-consul contains links to micronaut docs'() {
        when:
        def output = generate(['discovery-consul'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.consul.io")
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#serviceDiscoveryConsul")
    }

    @Unroll
    void 'test gradle discovery-consul feature for language=#language and buildTool=#buildTool'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['discovery-consul'])
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

    void 'test discovery-consul configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-consul'])

        then:
        commandContext.configuration.get('consul.client.registration.enabled'.toString()) == true
        commandContext.configuration.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'
    }

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/508")
    void 'discovery-consul with config-consul only adds configuration to bootstrap.yml'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-consul', 'config-consul'])

        then:
        commandContext.configuration.get('consul.client.registration.enabled'.toString()) == true
        commandContext.bootstrapConfiguration.get('consul.client.defaultZone') == '${CONSUL_HOST:localhost}:${CONSUL_PORT:8500}'

        !commandContext.bootstrapConfiguration.containsKey('consul.client.registration.enabled')
        !commandContext.configuration.containsKey('consul.client.defaultZone')
    }

}
