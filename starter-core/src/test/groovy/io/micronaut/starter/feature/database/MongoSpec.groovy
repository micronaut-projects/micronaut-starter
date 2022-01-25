package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class MongoSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "test add data-mongodb feature"() {
        when:
        def output = generate(['data-mongodb'])
        def readme = output["README.md"]
        def build = output['build.gradle']

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html")
        readme.contains("https://micronaut-projects.github.io/micronaut-data/3.3.x/guide/#mongo")
        readme.contains("https://docs.mongodb.com")
        build.contains('implementation("io.micronaut.data:micronaut-data-mongodb')
        build.contains('annotationProcessor("io.micronaut.data:micronaut-data-document-processor')

    }

    void 'test readme.md with feature mongo-sync contains links to micronaut and 3rd party docs'() {
        when:
        def output = generate(['mongo-sync'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html")
        readme.contains("https://docs.mongodb.com")
    }

    void "test mongo sync features"() {
        when:
        Features features = getFeatures(['mongo-sync'])

        then:
        features.contains("mongo-sync")
    }

    void "test mongo sync dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["mongo-sync"])
                .render()

        then:
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-sync")')
        template.contains('testImplementation("org.testcontainers:mongodb")')
    }

    void "test mongo sync dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['mongo-sync'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.mongodb</groupId>
      <artifactId>micronaut-mongo-sync</artifactId>
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
    }

    void 'test readme with feature mongo-reactive contains links to micronaut and 3rd party docs'() {
        when:
        def output = generate(['mongo-reactive'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/index.html")
        readme.contains("https://docs.mongodb.com")
    }

    void "test mongo reactive features"() {
        when:
        Features features = getFeatures(['mongo-reactive'])

        then:
        features.contains("mongo-reactive")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["mongo-reactive"])
                .render()
        then:
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        template.contains('testImplementation("org.testcontainers:mongodb")')
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['mongo-reactive'])
                .render()

        then:
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
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['mongo-reactive'])

        then:
        ctx.getConfiguration().get("mongodb.uri") == "mongodb://\${MONGO_HOST:localhost}:\${MONGO_PORT:27017}/mydb"
    }
}
