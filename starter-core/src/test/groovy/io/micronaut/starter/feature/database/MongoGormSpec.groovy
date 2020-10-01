package io.micronaut.starter.feature.database


import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class MongoGormSpec extends BeanContextSpec {

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
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-gorm"]), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-mongo-gorm")')
        template.contains('implementation("io.micronaut.mongodb:micronaut-mongo-reactive")')
        template.contains("testImplementation(\"de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.0.1\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(["mongo-gorm"]), []).render().toString()

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
      <groupId>de.flapdoodle.embed</groupId>
      <artifactId>de.flapdoodle.embed.mongo</artifactId>
      <version>2.0.1</version>
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
