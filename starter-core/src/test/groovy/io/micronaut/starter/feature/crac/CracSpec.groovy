package io.micronaut.starter.feature.crac

import groovy.xml.XmlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.aop.AOP
import io.micronaut.starter.feature.database.jdbc.Hikari
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Shared

class CracSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    Crac crac = beanContext.getBean(Crac)

    void 'test readme.md with feature crac contains links to micronaut docs'() {
        when:
        def output = generate([Crac.NAME])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Support for CRaC (Coordinated Restore at Checkpoint) documentation](https://micronaut-projects.github.io/micronaut-crac/latest/guide)")
        readme.contains("[https://wiki.openjdk.org/display/CRaC](https://wiki.openjdk.org/display/CRaC)")
    }

    void "test singletons are eager for #language"(Language language) {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(language), [Crac.NAME])
        def application = output[language.getSourcePath("/example/micronaut/Application")]

        then:
        application.contains(".eagerInitSingletons(true)")

        where:
        language << Language.values()
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

    void "test #buildTool crac feature for #language adds plugin and dependency"(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["crac"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.crac", "micronaut-crac", Scope.COMPILE)
        verifier.hasBuildPlugin("io.micronaut.crac")
        verifier.hasBuildPlugin("io.micronaut.application")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.valuesGradle()].combinations()
    }

    void "test maven crac feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["crac"])
                .render()
        def pom = new XmlSlurper().parseText(template)

        then:
        with(pom.dependencies.dependency.find { it.artifactId == "micronaut-crac" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.crac'
        }
    }

    void "test crac without hikari feature doesn't add configuration"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([Crac.NAME])

        then:
        !ctx.configuration.containsKey("datasources.default.allow-pool-suspension")
    }

    void "test crac and hikari feature adds configuration"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([Crac.NAME, Hikari.NAME])

        then:
        ctx.configuration.containsKey("datasources.default.allow-pool-suspension")
        ctx.configuration."datasources.default.allow-pool-suspension" == true
    }
}
