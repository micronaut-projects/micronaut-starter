package io.micronaut.starter.core.test.feature.opensearch

import io.micronaut.starter.feature.opensearch.OpenSearchAmazon
import io.micronaut.starter.feature.opensearch.OpenSearchFeature
import io.micronaut.starter.feature.opensearch.OpenSearchHttpClient5
import io.micronaut.starter.feature.opensearch.OpenSearchRestClient
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec

class OpenSearchFunctionalSpec extends CommandSpec {

    void 'test #featureName for #lang and #buildTool'(String featureName, Language lang, BuildTool buildTool) {
        given:
        generateProject(lang, buildTool, [featureName])

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output.contains("BUILD SUCCESS")
        output.contains("Loaded 2 test resources resolvers: io.micronaut.testresources.opensearch.OpenSearchTestResourceProvider")

        where:
        [featureName, lang, buildTool] << [
                [OpenSearchAmazon.NAME, OpenSearchHttpClient5.NAME, OpenSearchRestClient.NAME],
                Language.values(),
                BuildTool.values()
        ].combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        return "test-app-opensearch"
    }
}
