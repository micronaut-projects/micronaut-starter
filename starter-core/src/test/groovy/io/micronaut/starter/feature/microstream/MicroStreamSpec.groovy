package io.micronaut.starter.feature.microstream

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class MicroStreamSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature microstream contains links to micronaut docs'() {
        when:
        def output = generate(['microstream'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut MicroStream documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide)")
        readme.contains("[https://microstream.one/](https://microstream.one/)")
    }

    void 'test readme.md with feature microstream-rest contains links to micronaut docs'() {
        when:
        def output = generate(['microstream-rest'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut MicroStream documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide)")
        readme.contains("[https://microstream.one/](https://microstream.one/)")
        readme.contains("[Micronaut MicroStream REST documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide/#rest)")
        readme.contains("[https://docs.microstream.one/manual/storage/rest-interface/index.html](https://docs.microstream.one/manual/storage/rest-interface/index.html)")
    }

    void 'test readme.md with feature microstream-cache contains links to micronaut docs'() {
        when:
        def output = generate(['microstream-cache'])
        def readme = output["README.md"]

        then:
        !readme.contains("[Micronaut MicroStream documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide)")
        !readme.contains("[https://microstream.one/](https://microstream.one/)")
        readme.contains("[Micronaut MicroStream Cache documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide/#cache)")
        readme.contains("[https://docs.microstream.one/manual/cache/index.html](https://docs.microstream.one/manual/cache/index.html)")
    }

    void "test dependencies added for microstream feature"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['microstream'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        assertMicroStreamDependencies(verifier, buildTool, language)
        verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-annotations", Scope.ANNOTATION_PROCESSOR, 'micronaut.microstream.version', true)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void "test dependencies added for microstream-rest feature"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['microstream-rest'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        assertMicroStreamDependencies(verifier, buildTool, language)
        verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-rest", Scope.DEVELOPMENT_ONLY)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void "test dependencies added for microstream-cache feature"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['microstream-cache'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-cache", Scope.COMPILE)

        and: 'cache does not add core microstream'
        !verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void 'test microstream configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream'])

        then: "we don't configure storage as there are no sensible defaults"
        !commandContext.configuration.keySet().any { it.startsWith('microstream.storage.') }
    }

    void 'test microstream-rest configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream-rest'])

        then: "no configuration is created"
        !commandContext.configuration.keySet().any { it.startsWith('microstream.storage.') }
        !commandContext.configuration.keySet().any { it.startsWith('microstream.rest.') }
    }

    void 'test microstream-cache configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream-cache'])

        then:
        !commandContext.configuration.keySet().any { it.startsWith('microstream.storage.') }
        !commandContext.configuration.keySet().any { it.startsWith('microstream.rest.') }
        commandContext.configuration.'microstream.cache.my-cache.key-type' == 'java.lang.Integer'
        commandContext.configuration.'microstream.cache.my-cache.value-type' == 'java.lang.String'
    }

    @Unroll
    void 'test feature is not preview for #microStreamFeature.name'(MicroStreamFeature microStreamFeature) {
        expect:
        !microStreamFeature.isPreview()

        where:
        microStreamFeature << beanContext.getBeansOfType(MicroStreamFeature).iterator()
    }

    private void assertMicroStreamDependencies(BuildTestVerifier verifier, BuildTool buildTool, Language language) {
        verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream", Scope.COMPILE)
        if (buildTool == BuildTool.MAVEN && language == Language.GROOVY) {
            assert !verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-annotations", Scope.COMPILE)
        } else {
            assert verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-annotations", Scope.COMPILE)
        }
        assert verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-annotations", Scope.ANNOTATION_PROCESSOR, 'micronaut.microstream.version', true)
    }
}
