package io.micronaut.starter.feature.database

import groovy.xml.XmlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class DataAzureCosmosFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    DataAzureCosmosFeature feature = beanContext.getBean(DataAzureCosmosFeature)

    void 'test README.md with feature data-azure-cosmos contains links to micronaut and microsoft docs'() {
        when:
        def output = generate(['data-azure-cosmos'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Data Azure Cosmos documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/#azureCosmos)")
        readme.contains("[https://learn.microsoft.com/en-us/azure/cosmos-db/](https://learn.microsoft.com/en-us/azure/cosmos-db/)")
    }

    void "feature data-azure-cosmos properties validation"() {
        expect:
        for (applicationType in ApplicationType.values())
            feature.supports(applicationType) == true
        feature.category == Category.DATABASE
        feature.name == 'data-azure-cosmos'
    }

    void "test maven data-azure-cosmos feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["data-azure-cosmos"])
                .render()
        template.count('''
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
''') == 1
        def pom = new XmlSlurper().parseText(template)

        then:
        with(pom.dependencies.dependency.find { it.artifactId == "micronaut-data-azure-cosmos" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.data'
        }
    }

    void "test gradle data-azure-cosmos feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(["data-azure-cosmos"])
                .render()

        then:
        template.contains('implementation("io.micronaut.data:micronaut-data-azure-cosmos")')
        template.contains('annotationProcessor("io.micronaut.data:micronaut-data-document-processor")')
    }

    void "test data-azure-cosmos feature configuration"() {
        when:
        def config = buildGeneratorContext([feature.name]).configuration
        then:
        config.containsKey("azure.cosmos.endpoint")
        config.containsKey("azure.cosmos.key")
        config.containsKey("azure.cosmos.default-gateway-mode")
        config.containsKey("azure.cosmos.consistency-level")
        config.containsKey("azure.cosmos.endpoint-discovery-enabled")
        config.containsKey("azure.cosmos.database.database-name")
        config.containsKey("azure.cosmos.database.update-policy")
        config["azure.cosmos.database.database-name"] == "myDb"
        config["azure.cosmos.database.update-policy"] == "NONE"
    }
}