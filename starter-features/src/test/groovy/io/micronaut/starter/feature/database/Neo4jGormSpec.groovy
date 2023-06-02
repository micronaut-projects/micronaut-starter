package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class Neo4jGormSpec extends ApplicationContextSpec {

    void "test neo4j gorm features"() {
        when:
        Features features = getFeatures(['neo4j-gorm'])

        then:
        features.contains("groovy")
        features.contains("neo4j-bolt")
        features.contains("neo4j-gorm")
    }

    void "test dependencies are present for gradle"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["neo4j-gorm"])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains('implementation("io.micronaut.groovy:micronaut-neo4j-gorm")')
        template.contains('implementation("io.micronaut.neo4j:micronaut-neo4j-bolt")')
        template.contains("testRuntimeOnly(\"org.neo4j.test:neo4j-harness\")")
    }

    void "test dependencies are present for maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['neo4j-gorm'])
                .language(Language.GROOVY)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.groovy</groupId>
      <artifactId>micronaut-neo4j-gorm</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.neo4j</groupId>
      <artifactId>micronaut-neo4j-bolt</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>org.neo4j.test</groupId>
      <artifactId>neo4j-harness</artifactId>
      <scope>test</scope>
    </dependency>
""")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['neo4j-gorm'])

        then:
        ctx.getConfiguration().get("neo4j.uri") == "bolt://\${NEO4J_HOST:localhost}"
    }
}
