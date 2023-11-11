package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.aop.AOP
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class HibernateJpaSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature hibernate-jpa contains links to micronaut docs'() {
        when:
        def output = generate(['hibernate-jpa'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate")
    }

    void "test hibernate jpa features"() {
        when:
        Features features = getFeatures(['hibernate-jpa'])

        then:
        features.contains("h2")
        features.contains("jdbc-hikari")
        features.contains("hibernate-jpa")
    }

    void "test dependencies are present for #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['hibernate-jpa'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.sql", "micronaut-hibernate-jpa", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-tx-hibernate", Scope.COMPILE)
        verifier.hasDependency("com.h2database", "h2", Scope.RUNTIME)
        verifier.hasDependency("io.micronaut.sql", "micronaut-jdbc-hikari", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void "test kotlin jpa plugin is present for gradle kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["hibernate-jpa"])
                .language(Language.KOTLIN)
                .render()

        String pluginId = 'org.jetbrains.kotlin.plugin.jpa'
        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        template.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, template).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

    }

    void "test kotlin jpa plugin is present for maven kotlin project"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['hibernate-jpa'])
                .language(Language.KOTLIN)
                .render()

        then:
        //src/main
        template.contains("""
          <compilerPlugins>
            <plugin>jpa</plugin>
            <plugin>all-open</plugin>
          </compilerPlugins>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['hibernate-jpa'])

        then:
        ctx.configuration.containsKey('datasources.default.url')
        ctx.configuration.containsKey('jpa.default.properties.hibernate.hbm2ddl.auto')
    }
}

