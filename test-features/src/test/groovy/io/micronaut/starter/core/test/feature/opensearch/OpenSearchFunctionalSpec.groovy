package io.micronaut.starter.core.test.feature.opensearch

import io.micronaut.starter.feature.opensearch.OpenSearchFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec

class OpenSearchFunctionalSpec extends CommandSpec {

    void 'test #feature.name for #lang and #buildTool'(OpenSearchFeature feature, Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [feature.name])

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")
        output.contains("Loaded 2 test resources resolvers: io.micronaut.testresources.opensearch.OpenSearchTestResourceProvider")

        where:
        [feature, lang, buildTool] << [
            beanContext.getBeansOfType(OpenSearchFeature),
            Language.values(),
            BuildTool.values()
        ].combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-app-opensearch"
    }
}
