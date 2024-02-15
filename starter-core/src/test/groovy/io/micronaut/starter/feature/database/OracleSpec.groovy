package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class OracleSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test #buildTool oracle feature for language=#language'(BuildTool buildTool, Language language) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['oracle'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)
        then:
        verifier.hasDependency("com.oracle.database.jdbc", "ojdbc11", Scope.RUNTIME)

        where:
        [buildTool, language] << [BuildTool.values(), Language.values()].combinations()
    }
}
