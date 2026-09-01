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
        [opensearchFeature, buildTool] << [beanContext.getBeansOfType(OpenSearchFeature), BuildTool.values().toList()].combinations().findAll {
            // OpenSearch Rest Client requires Jackson Databind, which is not supported for Python because it uses Java reflection.
            !(it[0] instanceof OpenSearchRestClient && it[1] == BuildTool.PYRONAUT)
        }
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
        [opensearchFeature, buildTool] << [beanContext.getBeansOfType(OpenSearchFeature), BuildTool.values().toList()].combinations().findAll {
            // OpenSearch Rest Client requires Jackson Databind, which is not supported for Python because it uses Java reflection.
            !(it[0] instanceof OpenSearchRestClient && it[1] == BuildTool.PYRONAUT)
        }
    }

    void "test opensearch feature #opensearchFeature.name is search engine category"(OpenSearchFeature opensearchFeature) {
        expect:
        Category.SEARCH == opensearchFeature.getCategory()
        where:
        opensearchFeature << beanContext.getBeansOfType(OpenSearchFeature)
    }

    void "test opensearch feature #opensearchFeature.name documentation links"(OpenSearchFeature opensearchFeature) {
        expect:
        opensearchFeature.getMicronautDocumentation() == 'https://micronaut-projects.github.io/micronaut-opensearch/latest/guide/'
        opensearchFeature.getThirdPartyDocumentation() == 'https://opensearch.org/docs/latest/clients/java/'

        where:
        opensearchFeature << beanContext.getBeansOfType(OpenSearchFeature)
    }

    boolean isConfiguredForTestResources(BuildTool buildTool, BuildTestVerifier verifier, String template) {
        if (buildTool == BuildTool.PYRONAUT) {
            return verifier.hasBuildPlugin("io.micronaut.test-resources") &&
                    template.contains('additional-modules = ["opensearch"]')
        }
        buildTool == BuildTool.MAVEN ?
            verifier.hasTestResourceDependency("micronaut-test-resources-opensearch") :
            verifier.hasBuildPlugin("io.micronaut.test-resources") &&
                    template.contains('''testResources {
                                    |        additionalModules.add("opensearch")'''.stripMargin())
    }
}
