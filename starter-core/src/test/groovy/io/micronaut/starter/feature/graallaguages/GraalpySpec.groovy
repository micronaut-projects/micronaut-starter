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

    void 'readme.md with feature micronaut-graalpy contains links to docs'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_21), [Graalpy.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-graal-languages/latest/guide");
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

    void "micronaut-graalpy feature adds micronaut-graalpy dependency for Java and Maven "() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([Graalpy.NAME])
                .language(Language.JAVA)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, Language.JAVA, template)

        then:
        template
        verifier.hasDependency("io.micronaut.graal-languages", "micronaut-graalpy", Scope.COMPILE)
    }

    void "micronaut-graalpy feature adds maven-graalvm-plugin for language=java buildTool=maven "() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([Graalpy.NAME])
                .language(Language.JAVA)
                .render()

        then:
        template
        template.contains("<artifactId>graalpy-maven-plugin</artifactId>")
    }

    void "micronaut-graalpy feature requires java 21"() {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .jdkVersion(JdkVersion.JDK_17)
                .features([Graalpy.NAME])
                .render()

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "The selected feature graalpy requires at latest Java 21"
    }

    void 'test feature graalpy is only supported for Maven'() {
        given:
        String featureName = 'graalpy'
        when:
        getFeatures([featureName], new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_21))

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature only supported by Maven")
    }
}
