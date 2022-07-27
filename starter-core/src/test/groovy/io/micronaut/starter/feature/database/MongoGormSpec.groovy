package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class MongoGormSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature mongo-sync contains links to micronaut and 3rd party docs'() {
        when:
        Options options = new Options(Language.GROOVY, BuildTool.DEFAULT_OPTION)
        def output = generate(ApplicationType.DEFAULT,options, ['mongo-gorm'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html")
        readme.contains("https://gorm.grails.org/latest/mongodb/manual/")
        readme.contains("https://docs.mongodb.com")
    }

    void "test mongo gorm features"() {
        when:
        Features features = getFeatures(['mongo-gorm'])

        then:
        features.contains("groovy")
        features.contains("mongo-reactive")
        features.contains("mongo-gorm")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["mongo-gorm"])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-mongo-gorm")')
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
    }

    void "test testcontainers dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([TestContainers.NAME, 'mongo-gorm'])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-mongo-gorm")')
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        template.contains('testImplementation("org.testcontainers:mongodb")')
        template.contains('testImplementation("org.testcontainers:spock")')

    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestContainers.NAME, 'mongo-gorm'])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.groovy</groupId>
      <artifactId>micronaut-mongo-gorm</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.mongodb</groupId>
      <artifactId>micronaut-mongo-reactive</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>mongodb</artifactId>
      <scope>test</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>spock</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['mongo-gorm'])

        then:
        !ctx.getConfiguration().get("mongodb.uri")
    }
}
