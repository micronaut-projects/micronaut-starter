package io.micronaut.starter.feature.langchain4j

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class Langchain4jEmbeddedStoreSpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll("feature #featureName adds compile dependency #dependency")
    void "langchain4j embedded store dependencies"(String featureName, String groupId, String artifactId, String dependency) {
            given:
            Language language = Language.JAVA
            BuildTool buildTool = BuildTool.GRADLE

            when:
            String template = new BuildBuilder(beanContext, buildTool)
                    .language(language)
                    .features([featureName, "test-resources"])
                    .render()
            BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

            then:
            verifier.hasDependency(groupId, artifactId, Scope.COMPILE)
            verifier.hasDependency("io.micronaut.langchain4j", "micronaut-langchain4j-processor", Scope.ANNOTATION_PROCESSOR)
            if (featureName == "langchain4j-store-qdrant") {
                assert verifier.hasDependency("io.micronaut.langchain4j", "micronaut-langchain4j-qdrant-testresource", Scope.TEST_RESOURCES_SERVICE)
            }
            where:
            featureName | groupId | artifactId
            "langchain4j-store-elasticsearch" | "io.micronaut.langchain4j" | "micronaut-langchain4j-store-elasticsearch"
            "langchain4j-store-mongodb-atlas" | "io.micronaut.langchain4j" | "micronaut-langchain4j-store-mongodb-atlas"
            "langchain4j-store-neo4j" | "io.micronaut.langchain4j" | "micronaut-langchain4j-store-neo4j"
            "langchain4j-store-opensearch" | "io.micronaut.langchain4j" | "micronaut-langchain4j-store-opensearch"
            "langchain4j-store-qdrant" | "io.micronaut.langchain4j" | "micronaut-langchain4j-store-qdrant"
            dependency = "${groupId}:${artifactId}"
        }

    @Unroll("feature #featureName adds compile dependency #dependency")
    void "langchain4j embedded store dependencies"(String featureName, String groupId, String artifactId, String dependency) {
        given:
        Language language = Language.JAVA
        BuildTool buildTool = BuildTool.GRADLE

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([featureName])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency(groupId, artifactId, Scope.COMPILE)
        verifier.hasDependency("io.micronaut.langchain4j", "micronaut-langchain4j-processor", Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency("io.micronaut.sql", "micronaut-jdbc-hikari", Scope.COMPILE)

        where:
        featureName | groupId | artifactId
        "langchain4j-store-oracle" | "io.micronaut.langchain4j" | "micronaut-langchain4j-store-oracle"
        "langchain4j-store-pgvector" | "io.micronaut.langchain4j" | "micronaut-langchain4j-store-pgvector"
        dependency = "${groupId}:${artifactId}"
    }
}