package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture

class Neo4jBoltSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature neo4j-bolt contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['neo4j-bolt'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-neo4j/latest/guide/index.html")
        readme.contains("https://neo4j.com/docs/java-manual/current/")
    }

    void "test neo4j bolt features"() {
        when:
        Features features = getFeatures(['neo4j-bolt'])

        then:
        features.contains("neo4j-bolt")
    }

    void "test config"() {
        when:
        GeneratorContext ctx = buildGeneratorContext(['neo4j-bolt'])

        then:
        ctx.getConfiguration().get("neo4j.uri") == "bolt://\${NEO4J_HOST:localhost}"
    }
}
