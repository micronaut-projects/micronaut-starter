package io.micronaut.starter.feature.guice

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class MicronautGuiceSpec  extends ApplicationContextSpec implements CommandOutputFixture {
    void 'test readme.md with feature guice contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([MicronautGuice.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-guice/latest/guide/index.html")
    }

    void 'test #buildTool guice feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([MicronautGuice.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.guice", "micronaut-guice", Scope.COMPILE)
        verifier.hasAnnotationProcessor("io.micronaut.guice", "micronaut-guice-processor")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }

    void "guice feature is in the dependency injection category"() {
        given:
        String feature = MicronautGuice.NAME

        when:
        Optional<Feature> featureOptional = findFeatureByName(feature)

        then:
        featureOptional.isPresent()

        when:
        Feature f = featureOptional.get()

        then:
        f.category == "Dependency Injection"

        and: 'supports every application type'
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert f.supports(applicationType)
        }
    }
}
