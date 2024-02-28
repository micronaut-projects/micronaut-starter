package io.micronaut.starter.feature.eclipsestore

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

class EclipseStoreSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature eclipsestore contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['eclipsestore'])
        String readme = output["README.md"]

        then:
        readme.contains("[Micronaut EclipseStore documentation](https://micronaut-projects.github.io/micronaut-eclipsestore/latest/guide)")
        readme.contains("[https://docs.eclipsestore.io/](https://docs.eclipsestore.io/)")
    }

    void 'test readme.md with feature eclipsestore-rest contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['eclipsestore-rest'])
        String readme = output["README.md"]

        then:
        readme.contains("[Micronaut EclipseStore documentation](https://micronaut-projects.github.io/micronaut-eclipsestore/latest/guide)")
        readme.contains("[https://docs.eclipsestore.io/](https://docs.eclipsestore.io/)")
        readme.contains("[Micronaut EclipseStore REST documentation](https://micronaut-projects.github.io/micronaut-eclipseStore/latest/guide/#rest)")
        readme.contains("[https://docs.eclipsestore.io/manual/storage/rest-interface/index.html](https://docs.eclipsestore.io/manual/storage/rest-interface/index.html)")
    }

    void "test dependencies added for eclipsestore feature for #language and build #buildTool"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['eclipsestore'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        assertEclipseStoreDependencies(verifier, buildTool, language)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

    void "test dependencies added for eclipsestore-rest feature for #language and build #buildTool"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['eclipsestore-rest'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        assertEclipseStoreDependencies(verifier, buildTool, language)
        verifier.hasDependency("io.micronaut.eclipsestore", "micronaut-eclipsestore-rest", Scope.DEVELOPMENT_ONLY)

        where:
        [language, buildTool] << [[Language.GROOVY], [BuildTool.MAVEN]].combinations()//[Language.values(), BuildTool.values()].combinations()
    }

    void 'test eclipsestore configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['eclipsestore'])

        then: "we don't configure storage as there are no sensible defaults"
        !commandContext.configuration.keySet().any { it.startsWith('eclipsestore.storage.') }
    }

    void 'test eclipsestore-rest configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['eclipsestore-rest'])

        then: "no configuration is created"
        !commandContext.configuration.keySet().any { it.startsWith('eclipsestore.storage.') }
        !commandContext.configuration.keySet().any { it.startsWith('eclipsestore.rest.') }
    }

    @Unroll
    void 'test feature is not preview for #eclipsestoreFeature.name'(EclipseStoreFeature eclipsestoreFeature) {
        expect:
        !eclipsestoreFeature.isPreview()

        where:
        eclipsestoreFeature << beanContext.getBeansOfType(EclipseStoreFeature).iterator()
    }

    private static void assertEclipseStoreDependencies(BuildTestVerifier verifier, BuildTool buildTool, Language language) {
        assert verifier.hasDependency("io.micronaut.eclipsestore", "micronaut-eclipsestore", Scope.COMPILE)
        assert !verifier.hasDependency("io.micronaut.eclipsestore", "micronaut-eclipsestore-annotations")
        assert !verifier.hasDependency("io.micronaut.eclipsestore", "micronaut-eclipsestore-processor", Scope.COMPILE)
        assert verifier.hasDependency("io.micronaut.eclipsestore", "micronaut-eclipsestore-processor", Scope.ANNOTATION_PROCESSOR, 'micronaut.eclipsestore.version', true)
    }
}
