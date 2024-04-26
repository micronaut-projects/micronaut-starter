package io.micronaut.starter.feature.sourcegen

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared

class SourcegenJavaSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    @Shared
    SourcegenJava sourcegenJava = beanContext.getBean(SourcegenJava)

    void 'test readme.md with feature sourcegen-generator contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([SourcegenJava.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sourcegen/latest/guide/")
    }

    void "feature sourcegen-generator supports applicationType=#applicationType"(ApplicationType applicationType) {
        expect:
        sourcegenJava.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void "feature sourcegen-generator feature is API category"() {
        expect:
        sourcegenJava.category == Category.API
    }

    void 'sourcegen-generator feature not supported for groovy'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([SourcegenJava.NAME])
                .language(Language.GROOVY)
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message == 'sourcegen-generator is not supported in Groovy applications'
    }


    void "test dependencies are present for #buildTool and #language"(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([SourcegenJava.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.sourcegen", "micronaut-sourcegen-annotations", Scope.COMPILE)
        if (language == Language.JAVA) {
            assert verifier.hasAnnotationProcessor("io.micronaut.sourcegen", "micronaut-sourcegen-generator-java")
        } else if (language == Language.KOTLIN) {
            assert verifier.hasAnnotationProcessor("io.micronaut.sourcegen", "micronaut-sourcegen-generator-kotlin")
        }

        where:
        [buildTool, language] << [BuildTool.values(), [Language.JAVA, Language.KOTLIN]].combinations()
    }
}
