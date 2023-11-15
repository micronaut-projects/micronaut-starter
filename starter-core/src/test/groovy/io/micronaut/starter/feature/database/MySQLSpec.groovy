package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.Language

import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN

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
        when:
        String template = new BuildBuilder(beanContext, MAVEN)
                .features(['mysql'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <scope>runtime</scope>
    </dependency>
""")

        and:
        template.contains("""
            <testResourcesDependencies>
              <dependency>
                <groupId>io.micronaut.testresources</groupId>
                <artifactId>micronaut-test-resources-jdbc-mysql</artifactId>
              </dependency>
            </testResourcesDependencies>
""")
        where:
        language << Language.values().toList()
    }

}
