package io.micronaut.starter.feature.database

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
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

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["hibernate-jpa"])
                .render()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-hibernate-jpa")')
        template.contains("runtimeOnly(\"com.h2database:h2\")")
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

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['hibernate-jpa'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-hibernate-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
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

