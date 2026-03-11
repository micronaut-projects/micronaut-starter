package io.micronaut.starter.feature.graallaguages

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.graallanguages.Graalpy
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class GraalpySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Graalpy micronautGraalPyFeature = beanContext.getBean(Graalpy)

    void "readme.md with feature micronaut-graalpy contains links to docs for language=#language buildTool=#buildTool "(BuildTool buildTool, Language language) {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(language, TestFramework.JUNIT, buildTool, JdkVersion.JDK_25), [Graalpy.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-graal-languages/latest/guide");
        readme.contains("https://graalvm.org/python");

        where:
        [buildTool, language] << [BuildTool.values(), Language.JAVA].combinations()
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
                .features([Graalpy.NAME])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        template
        verifier.hasDependency("io.micronaut.graal-languages", "micronaut-graalpy", Scope.COMPILE)
        if (buildTool == BuildTool.MAVEN) {
            assert template.contains("<artifactId>graalpy-maven-plugin</artifactId>")
        } else if (buildTool.isGradle()) {
            assert verifier.hasBuildPlugin("org.graalvm.python")
        }

        where:
        [buildTool, language] << [BuildTool.values(), Language.JAVA].combinations()
    }

    void "micronaut-graalpy feature requires java 25"() {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .jdkVersion(JdkVersion.JDK_17)
                .features([Graalpy.NAME])
                .render()

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "The selected feature graalpy requires at latest Java 25"
    }

}
