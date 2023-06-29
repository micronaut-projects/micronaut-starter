package io.micronaut.starter.feature.objectstorage

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class ObjectStorageOracleCloudSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "object-storage-oracle-cloud has the correct properties"() {
        given:
        ObjectStorageOracleCloud feature = beanContext.streamOfType(ObjectStorageOracleCloud).findFirst().orElse(null)

        expect:
        feature.name == "object-storage-oracle-cloud"
        feature.title == "Object Storage - Oracle Cloud"
        feature.description == "Micronaut Object Storage provides a uniform API to create, read and delete objects in the major cloud providers. This feature adds the Oracle Cloud implementation"
        feature.category == Category.CLOUD
        feature.cloud == Cloud.ORACLE
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert feature.supports(applicationType)
        }
    }

    void 'test #buildTool object-storage-oracle-cloud feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["object-storage-oracle-cloud"])
                .render()

        then:
        template.contains('implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-sdk")')
        template.contains('implementation("io.micronaut.objectstorage:micronaut-object-storage-oracle-cloud")')

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.valuesGradle()].combinations()
    }

    void 'test maven object-storage-oracle-cloud feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(["object-storage-oracle-cloud"])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.oraclecloud</groupId>
      <artifactId>micronaut-oraclecloud-sdk</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.objectstorage</groupId>
      <artifactId>micronaut-object-storage-oracle-cloud</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
