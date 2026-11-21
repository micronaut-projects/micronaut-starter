package io.micronaut.starter.feature.mateu

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
import io.micronaut.starter.options.Options
import spock.lang.Shared

class MateuSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    Mateu mateu = beanContext.getBean(Mateu)

    void 'test readme.md with feature mateu contains links to docs'() {
        when:
        Map<String, String> output = generate([Mateu.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://mateu.io")
    }

    void 'mateu is a community feature in the View Rendering category'() {
        expect:
        mateu.community
        mateu.category == Category.VIEW
    }

    void 'mateu supports only the default application type'(ApplicationType applicationType) {
        expect:
        mateu.supports(applicationType) == (applicationType == ApplicationType.DEFAULT)

        where:
        applicationType << ApplicationType.values()
    }

    void 'mateu supports only Java'(Language language) {
        expect:
        mateu.supports(language) == (language == Language.JAVA)

        where:
        language << Language.values()
    }

    void 'dependencies and annotation processor are present for #buildTool'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([Mateu.NAME])
                .language(Language.JAVA)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, Language.JAVA, template)

        then:
        verifier.hasDependency("io.mateu", "mateu-micronaut", Scope.COMPILE)
        verifier.hasDependency("io.mateu", "mateu-redwood", Scope.RUNTIME)
        verifier.hasAnnotationProcessor("io.mateu", "mateu-annotation-processor-micronaut")

        where:
        buildTool << BuildTool.valuesGradle() + [BuildTool.MAVEN]
    }

    void 'a sample @UI class is generated'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA), [Mateu.NAME])
        String home = output["src/main/java/example/micronaut/Home.java"]

        then:
        home
        home.contains('@UI("")')
        home.contains("class Home")
    }
}
