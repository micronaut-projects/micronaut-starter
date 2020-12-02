package io.micronaut.starter.feature.database


import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture

class HibernateJpaSpec extends BeanContextSpec  implements CommandOutputFixture {

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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["hibernate-jpa"]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.sql:micronaut-hibernate-jpa")')
        template.contains("runtimeOnly(\"com.h2database:h2\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["hibernate-jpa"]), []).render().toString()

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

