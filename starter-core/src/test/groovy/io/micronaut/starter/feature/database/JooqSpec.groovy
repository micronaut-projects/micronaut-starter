package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.Options

class JooqSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jooq contains links to micronaut docs'() {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options().withJavaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION), ['jooq'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jooq")
    }

    void 'test buildTool=#buildTool jooq feature does not have jooq plugin for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['jooq'])
                .language(language)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency('io.micronaut.sql','micronaut-jooq', Scope.COMPILE)

        and:
        if (buildTool.isGradle()) {
             assert !verifier.hasBuildPlugin('org.jooq.jooq-codegen-gradle')
        }

        where:
        [buildTool,language] << [BuildTool.values()-BuildTool.MAVEN, Language.values()].combinations()
    }

    void 'test buildTool=#buildTool jooq feature does not have jooq plugin for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['jooq', dbType])
                .language(language)
                .jdkVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency('io.micronaut.sql','micronaut-jooq', Scope.COMPILE)

        and:
        if (buildTool.isGradle()) {
             assert verifier.hasBuildPlugin('org.jooq.jooq-codegen-gradle')
        }

        where:
        [buildTool,language, dbType] << [BuildTool.values()-BuildTool.MAVEN,
                                 Language.values(),
                                ['mariadb', 'mysql', 'oracle', 'postgres', 'sqlserver']]
                .combinations()
    }
}
