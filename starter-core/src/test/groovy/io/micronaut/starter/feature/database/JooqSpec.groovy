package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.LanguageUtils

class JooqSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jooq contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options().withJavaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION), ['jooq'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jooq")
    }

    void 'test gradle jooq feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jooq'])
                .language(language)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .render()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-jooq")')

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }

    void 'test maven jooq feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['jooq'])
                .language(language)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.sql", "micronaut-jooq", Scope.COMPILE)

        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }

    void "test jooq cannot be applied for #language with Java 8"() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jooq'])
                .language(language)
                .jdkVersion(JdkVersion.JDK_8)
                .render()

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "The selected feature jooq requires at latest Java 11"

        where:
        language << LanguageUtils.JVM_LANGUAGES
    }
}
