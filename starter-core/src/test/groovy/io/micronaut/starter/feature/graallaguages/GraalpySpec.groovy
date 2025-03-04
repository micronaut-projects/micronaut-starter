package io.micronaut.starter.feature.graallaguages

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.graallanguages.Graalpy
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class GraalpySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    Graalpy micronautGraalPyFeature = beanContext.getBean(Graalpy)

    void 'readme.md with feature micronaut-graalpy contains links to docs'() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.MAVEN)
                .javaVersion(JdkVersion.JDK_21)
                .features([Graalpy.NAME])
                .build()
        when:
        Map<String, String> output = generate(options)
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
        micronautGraalPyFeature.supports(MicronautOptions.builder().applicationType(applicationType).build())

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
        Options options = MicronautOptions.builder()
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(JdkVersion.JDK_21)
                .build()
        when:
        getFeatures([featureName], options)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature only supported by Maven")
    }
}
