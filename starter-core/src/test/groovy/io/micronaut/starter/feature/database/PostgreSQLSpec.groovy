package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class PostgreSQLSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle postgres feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['postgres'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("org.postgresql", "postgresql", Scope.RUNTIME)
        !verifier.hasDependency("org.apache.commons", "commons-compress", Scope.TEST)

        when:
        template = new BuildBuilder(beanContext, buildTool).features(['postgres', 'testcontainers']).language(language).render()
        verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.postgresql", "postgresql", Scope.RUNTIME)
        verifier.hasDependency("org.apache.commons", "commons-compress", Scope.TEST)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations().findAll { it -> supportedLanguages(it[1]).contains(it[0]) }
    }
}
