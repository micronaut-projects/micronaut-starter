package io.micronaut.starter.feature.migration

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.feature.lang.java.JavaApplicationRenderingContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class LiquibaseSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature liquibase contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['liquibase'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.liquibase.org")
        readme.contains("https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html")
    }

    void 'test feature liquibase contains configuration'() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'liquibase'])
        String changelog = output["src/main/resources/db/liquibase-changelog.xml"]
        String schema = output["src/main/resources/db/changelog/01-schema.xml"]
        String config = output["src/main/resources/application.yml"]

        then:
        changelog
        schema
        config
        config.contains("""
liquibase:
  datasources:
    default:
      change-log: classpath:db/liquibase-changelog.xml
""")

        when:
        String applicationJava = output['src/main/java/example/micronaut/Application.java']


        then:
        applicationJava.contains("""
package example.micronaut;

import org.slf4j.bridge.SLF4JBridgeHandler;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Micronaut.run(Application.class, args);
    }
}
""".trim())
    }

    void "test the dependency is added to the gradle build"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['liquibase'])
                .render()

        then:
        template.contains('implementation("io.micronaut.liquibase:micronaut-liquibase")')
    }

    void "test the dependency is added to the maven build"() {
        given:
        BuildTool buildTool = BuildTool.MAVEN
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['liquibase'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.liquibase", "micronaut-liquibase")
        verifier.hasDependency("org.slf4j", "jul-to-slf4j")
    }
}
