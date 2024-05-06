package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

import static io.micronaut.starter.options.BuildTool.GRADLE

class MySQLSpec extends ApplicationContextSpec {

    void 'test gradle mysql feature for language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, GRADLE)
                .features(['mysql'])
                .language(language)
                .render()

        then:
        template.contains('runtimeOnly("com.mysql:mysql-connector-j")')

        and:
        template.contains("""
    testResources {
        additionalModules.add("jdbc-mysql")
    }""")

        where:
        language << Language.values().toList()
    }

    void 'testresources not configured for Gradle with testContainers feature and language=#language'() {
        when:
        String template = new BuildBuilder(beanContext, GRADLE)
                .features(['mysql', 'testcontainers'])
                .language(language)
                .render()

        then:
        template.contains('runtimeOnly("com.mysql:mysql-connector-j")')

        and:
        !template.contains("""
    testResources {
        additionalModules.add("jdbc-mysql")
    }""")

        where:
        language << Language.values().toList()
    }

    void 'test maven mysql feature for language=#language'() {
        given:
        BuildTool buildTool = BuildTool.MAVEN
        when:

        String template = new BuildBuilder(beanContext, buildTool)
                .features(['mysql'])
                .language(language)
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency('com.mysql', "mysql-connector-j", Scope.RUNTIME)
        verifier.hasTestResourceDependency("micronaut-test-resources-jdbc-mysql")

        where:
        language << Language.values().toList()
    }
}
