package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class MariaDBSpec extends ApplicationContextSpec {

    @Unroll
    void 'test #buildTool mariadb feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool).features(['mariadb']).language(language).render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.mariadb.jdbc", "mariadb-java-client", Scope.RUNTIME)
        !verifier.hasDependency("org.apache.commons", "commons-compress", Scope.TEST)

        when:
        template = new BuildBuilder(beanContext, buildTool).features(['mariadb', 'testcontainers']).language(language).render()
        verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.mariadb.jdbc", "mariadb-java-client", Scope.RUNTIME)
        verifier.hasDependency("org.apache.commons", "commons-compress", Scope.TEST)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values().toList()].combinations().findAll { it -> supportedLanguages(it[1]).contains(it[0]) }
    }


}
