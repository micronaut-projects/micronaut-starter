package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

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

    void "neo4j-bolt dependency is present for build tool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['neo4j-bolt'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.neo4j","micronaut-neo4j-bolt")
        !verifier.hasDependency("org.testcontainers","neo4j", Scope.TEST)

        where:
        buildTool << BuildTool.values()
    }

    void "testcontainers neo4j-bolt dependency is present for build tool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['neo4j-bolt','testcontainers'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.testcontainers","neo4j", Scope.TEST)

        where:
        buildTool << BuildTool.values()
    }

    void "test neo4j-bolt test-resources for build tool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['neo4j-bolt','test-resources'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("org.testcontainers","neo4j", Scope.TEST)
        !verifier.hasDependency("org.testcontainers","testcontainers", Scope.TEST)
        verifier.hasBuildPlugin("io.micronaut.test-resources")

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "test neo4j-bolt test-resources for build tool Maven"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['neo4j-bolt','test-resources'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, template)

        then:
        !verifier.hasDependency("org.testcontainers","neo4j", Scope.TEST)
        !verifier.hasDependency("org.testcontainers","testcontainers", Scope.TEST)
        verifier.hasDependency("io.micronaut.testresources","micronaut-test-resources-client")
    }
}
