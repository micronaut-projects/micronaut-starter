package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Unroll

class SQLServerSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test #buildTool sqlserver feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['sqlserver'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("com.microsoft.sqlserver", "mssql-jdbc", Scope.RUNTIME)
        if (buildTool.isGradle()) {
            assert verifier.hasBuildPlugin("io.micronaut.test-resources")
        }

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }

    void 'test for test-resources the accept-license property is set to false'() {
        when:
        Map<String, String> output = generate(["sqlserver", "properties"])

        then:
        output["src/main/resources/application.properties"].contains("test-resources.containers.mssql.accept-license=false\n")
    }

    @Requires({ jvm.current.isJava11Compatible() })
    void 'test for test-resources the accept-license property is set to false with hibernate reactive=#hibernateReactiveFeature'(
            String hibernateReactiveFeature) {
        when:
        Map<String, String> output = generate(["sqlserver", "properties", hibernateReactiveFeature])

        then:
        output["src/main/resources/application.properties"].contains("test-resources.containers.mssql.accept-license=false\n")

        where:
        hibernateReactiveFeature << [HibernateReactiveJpa.NAME, DataHibernateReactive.NAME]
    }
}
