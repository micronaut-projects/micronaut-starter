package io.micronaut.starter.feature.opensearch

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.database.TestContainers
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class OpenSearchFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test opensearch feature #opensearchFeature.name contributes dependencies for #buildTool'(OpenSearchFeature opensearchFeature, BuildTool buildTool) {
        given:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([opensearchFeature.name])
                .render()
        when:
        String groupId = MicronautDependencyUtils.GROUP_ID_MICRONAUT_OPENSEARCH
        String artifactId = "micronaut-" + opensearchFeature.getName()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency(groupId, artifactId)
        if (opensearchFeature instanceof OpenSearchRestClient) {
            assert verifier.hasDependency("io.micronaut", "micronaut-jackson-databind")
        }

        and:
        isConfiguredForTestResources(buildTool, verifier, template)

        where:
        [opensearchFeature, buildTool] << [beanContext.getBeansOfType(OpenSearchFeature), BuildTool.values()].combinations()
    }

    void 'test opensearch feature #opensearchFeature.name contributes testcontainers dependencies for #buildTool'(OpenSearchFeature opensearchFeature, BuildTool buildTool) {
        given:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([opensearchFeature.name, TestContainers.NAME])
                .render()
        when:
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.opensearch", "opensearch-testcontainers", Scope.TEST)
        verifier.hasDependency("org.testcontainers", "testcontainers", Scope.TEST)

        and:
        !isConfiguredForTestResources(buildTool, verifier, template)

        where:
        [opensearchFeature, buildTool] << [beanContext.getBeansOfType(OpenSearchFeature), BuildTool.values()].combinations()
    }

    void "test opensearch feature #opensearchFeature.name is search engine category"(OpenSearchFeature opensearchFeature) {
        expect:
        Category.SEARCH == opensearchFeature.getCategory()
        where:
        [opensearchFeature, buildTool] << [beanContext.getBeansOfType(OpenSearchFeature), BuildTool.values()].combinations()
    }

    void "test opensearch feature #opensearchFeature.name documentation links"(OpenSearchFeature opensearchFeature) {
        expect:
        opensearchFeature.getMicronautDocumentation() == 'https://micronaut-projects.github.io/micronaut-opensearch/latest/guide/'
        opensearchFeature.getThirdPartyDocumentation() == 'https://opensearch.org/docs/latest/clients/java/'

        where:
        [opensearchFeature, buildTool] << [beanContext.getBeansOfType(OpenSearchFeature), BuildTool.values()].combinations()
    }

    boolean isConfiguredForTestResources(BuildTool buildTool, BuildTestVerifier verifier, String template) {
        buildTool == BuildTool.MAVEN ?
            verifier.hasTestResourceDependency("micronaut-test-resources-opensearch") :
            verifier.hasBuildPlugin("io.micronaut.test-resources") &&
                    template.contains('''testResources {
                                    |        additionalModules.add("opensearch")
                                    |    }'''.stripMargin())
    }
}
