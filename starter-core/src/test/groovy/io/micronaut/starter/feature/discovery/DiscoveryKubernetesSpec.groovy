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

class DiscoveryKubernetesSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature discovery-kubernetes contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['discovery-kubernetes'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#service-discovery")
    }

    @Unroll
    void 'test gradle discovery-kubernetes feature for language=#language and buildTool=#buildTool'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['discovery-kubernetes'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.kubernetes", "micronaut-kubernetes-discovery-client", Scope.COMPILE)

        and: 'It does not define a dependency on micronaut-discovery-core since it is pulled transitively'
        !verifier.hasDependency("io.micronaut", "micronaut-discovery-core", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations()
    }

    void 'test discovery-kubernetes configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['discovery-kubernetes'])

        then:
        commandContext.bootstrapConfiguration.get('kubernetes.client.discovery.mode-configuration.endpoint.watch.enabled'.toString()) == true
        commandContext.bootstrapConfiguration.get('kubernetes.client.discovery.mode') == 'endpoint'
    }

}
