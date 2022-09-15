package io.micronaut.starter.feature.crac

import groovy.xml.XmlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared

class CracSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    Crac crac = beanContext.getBean(Crac)

    void 'test readme.md with feature crac contains links to micronaut docs'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.DEFAULT_OPTION, TestFramework.DEFAULT_OPTION, BuildTool.DEFAULT_OPTION, JdkVersion.JDK_17), ['crac'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Support for CRaC (Coordinated Restore at Checkpoint) documentation](https://micronaut-projects.github.io/micronaut-crac/latest/guide)")
        readme.contains("[https://wiki.openjdk.org/display/CRaC](https://wiki.openjdk.org/display/CRaC)")
    }

    void "feature crac #desc for #applicationType"(ApplicationType applicationType) {
        expect:
        crac.supports(applicationType) == expected

        where:
        applicationType           | expected
        ApplicationType.CLI       | true
        ApplicationType.DEFAULT   | true
        ApplicationType.FUNCTION  | false
        ApplicationType.MESSAGING | false
        ApplicationType.GRPC      | false

        desc = expected ? "is supported" : "is not supported"
    }

    void "test #buildTool crac feature for #language adds plugin and dependency"() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .jdkVersion(JdkVersion.JDK_17)
                .language(language)
                .features(["crac"])
                .render()

        then:
        template.contains('id("io.micronaut.crac") version')
        template.contains('implementation("io.micronaut.crac:micronaut-crac")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    void "test maven crac feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .jdkVersion(JdkVersion.JDK_17)
                .features(["crac"])
                .render()
        def pom = new XmlSlurper().parseText(template)

        then:
        with(pom.dependencies.dependency.find { it.artifactId == "micronaut-crac" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.crac'
        }
    }

    void "crac and graal cannot be combined for #buildTool"() {
        when:
        new BuildBuilder(beanContext, buildTool)
                .jdkVersion(JdkVersion.JDK_17)
                .features(["crac", "graalvm"])
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message == "CRaC and GraalVM cannot be combined"

        where:
        buildTool << BuildTool.values()
    }

    void "Java less than 17 isn't compatible with CRaC (check #buildTool, #jdk)"() {
        when:
        new BuildBuilder(beanContext, buildTool)
                .jdkVersion(jdk)
                .features(["crac"])
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message == "CRaC needs at least JDK 17"

        where:
        [buildTool, jdk] << [BuildTool.values(), JdkVersion.values().findAll { it.compareTo(JdkVersion.JDK_17) < 0 }].combinations()
    }

    void "Java 17 or more is accepted (check #buildTool, #jdk)"() {
        when:
        new BuildBuilder(beanContext, buildTool)
                .jdkVersion(jdk)
                .features(["crac"])
                .render()

        then:
        noExceptionThrown()

        where:
        [buildTool, jdk] << [BuildTool.values(), JdkVersion.values().findAll { it.compareTo(JdkVersion.JDK_17) >= 0 }].combinations()
    }
}
