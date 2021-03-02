package io.micronaut.starter.feature.database

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.build.maven.MavenBuild
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class MongoGormSpec extends BeanContextSpec implements CommandOutputFixture {

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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-gorm"]), new GradleBuild()).render().toString()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-mongo-gorm")')
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
    }

    void "test testcontainers dependencies are present for gradle"() {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(),
                getFeatures(["mongo-gorm", "testcontainers"], Language.GROOVY, TestFramework.SPOCK), new GradleBuild()).render().toString()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-mongo-gorm")')
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        template.contains('testImplementation("org.testcontainers:mongodb")')
        template.contains('testImplementation("org.testcontainers:spock")')

    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-gorm"]), new MavenBuild()).render().toString()

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
        ctx.getConfiguration().get("mongodb.uri") == "mongodb://\${MONGO_HOST:localhost}:\${MONGO_PORT:27017}"
    }
}
