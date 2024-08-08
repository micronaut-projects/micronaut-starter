package io.micronaut.starter.feature.graallaguages

import com.fasterxml.jackson.core.JsonToken
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.graallanguages.MicronautGraalPyFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject

class MicronautGraalPyFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    MicronautGraalPyFeature micronautGraalPyFeature = beanContext.getBean(MicronautGraalPyFeature)

    void 'readme.md with feature micronaut-graalpy contains links to docs'() {
        when:
        Map<String, String> output = generate([MicronautGraalPyFeature.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-graalpy/latest/guide");
        readme.contains("https://graalvm.org/python");
    }

    void "micronaut-graalpy belongs to LANGUAGES category"() {
        expect:
        Category.LANGUAGES == micronautGraalPyFeature.category
    }

    void "micronaut-graalpy supports application type = #applicationType"(ApplicationType applicationType) {
        expect:
        micronautGraalPyFeature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "micronaut-graalpy feature adds micronaut-graalpy dependency for language=#language buildTool=#buildTool "(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([MicronautGraalPyFeature.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template
        verifier.hasDependency("io.micronaut.graal-languages", "micronaut-graalpy", Scope.COMPILE)

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }

    void "micronaut-graalpy feature adds maven-graalvm-plugin for language=java buildTool=maven "() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([MicronautGraalPyFeature.NAME])
                .language(Language.JAVA)
                .render()

        then:
        template
        template.contains("<artifactId>graalpy-maven-plugin</artifactId>")
    }
}
