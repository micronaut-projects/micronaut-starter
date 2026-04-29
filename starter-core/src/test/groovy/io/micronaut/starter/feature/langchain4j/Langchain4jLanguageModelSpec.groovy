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

class Langchain4jLanguageModelSpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll("feature #featureName adds compile dependency #dependency")
    void "langchain4j language model dependencies"(String featureName, String groupId, String artifactId, String dependency) {
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
        if (featureName == "langchain4j-ollama") {
            assert verifier.hasDependency("io.micronaut.langchain4j", "micronaut-langchain4j-ollama-testresources", Scope.TEST_RESOURCES_SERVICE)
        }

        where:
        featureName                              | groupId                                 | artifactId
        "langchain4j-anthropic"            |  "io.micronaut.langchain4j" | "micronaut-langchain4j-anthropic"
        "langchain4j-azure"                  |  "io.micronaut.langchain4j" | "micronaut-langchain4j-azure"
        "langchain4j-bedrock"              |  "io.micronaut.langchain4j" | "micronaut-langchain4j-bedrock"
        "langchain4j-hugging-face"     |  "io.micronaut.langchain4j" | "micronaut-langchain4j-hugging-face"
        "langchain4j-mistralai"              |  "io.micronaut.langchain4j" | "micronaut-langchain4j-mistralai"
        "langchain4j-ollama"                 |  "io.micronaut.langchain4j" | "micronaut-langchain4j-ollama"
        "langchain4j-openai"                 |  "io.micronaut.langchain4j" | "micronaut-langchain4j-openai"
        "langchain4j-googleai-gemini"  |  "io.micronaut.langchain4j" | "micronaut-langchain4j-googleai-gemini"
        "langchain4j-vertexai"               |  "io.micronaut.langchain4j" | "micronaut-langchain4j-vertexai"
        "langchain4j-vertexai-gemini"  |  "io.micronaut.langchain4j" | "micronaut-langchain4j-vertexai-gemini"
        dependency = "${groupId}:${artifactId}"
    }

}
