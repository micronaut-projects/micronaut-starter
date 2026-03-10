package io.micronaut.starter.feature.database.jdbc

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class JdbcSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void "test gradle jdbc feature #jdbcFeature"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([jdbcFeature])
                .render()

        then:
        template.contains("implementation(\"io.micronaut.sql:micronaut-${jdbcFeature}\")")
        template.contains("runtimeOnly(\"com.h2database:h2\")")

        when:
        template = template.replace("implementation(\"io.micronaut.sql:micronaut-${jdbcFeature}\")", "")

        then: "make sure we didn't add default JdbcFeature if some other was added"
        !template.contains("implementation(\"io.micronaut.sql:micronaut-jdbc-hikari\")")

        where:
        jdbcFeature << beanContext.getBeansOfType(JdbcFeature)*.name
    }

    @Unroll
    void "test maven jdbc feature #jdbcFeature"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([jdbcFeature])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        verifier.hasDependency("io.micronaut.sql", "micronaut-${jdbcFeature}", Scope.COMPILE)
        verifier.hasDependency("com.h2database", "h2", Scope.RUNTIME)

        where:
        jdbcFeature << beanContext.getBeansOfType(JdbcFeature)*.name
    }

    void "test there can only be one jdbc feature"() {
        when:
        getFeatures(beanContext.getBeansOfType(JdbcFeature)*.name)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

    void "test jdbc feature configuration"() {
        when:
        GeneratorContext ctx = buildGeneratorContext([jdbcFeature])

        then:
        ctx.configuration.containsKey("datasources.default.url")
        ctx.configuration.containsKey("datasources.default.driver-class-name")

        where:
        jdbcFeature << beanContext.getBeansOfType(JdbcFeature)*.name
    }

    @Unroll
    void 'test readme.md with feature #jdbcFeature contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate([jdbcFeature])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc")

        where:
        jdbcFeature << beanContext.getBeansOfType(JdbcFeature)*.name
    }
}
