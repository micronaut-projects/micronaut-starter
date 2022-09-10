package io.micronaut.starter.feature.objectstorage

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class ObjectStorageAzureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "object-storage-azure has the correct properties"() {
        given:
        ObjectStorageAzure feature = beanContext.streamOfType(ObjectStorageAzure).findFirst().orElse(null)

        expect:
        feature.name == "object-storage-azure"
        feature.title == "Object Storage - Azure"
        feature.description == "Micronaut Object Storage provides a uniform API to create, read and delete objects in the major cloud providers. This feature adds the Azure implementation"
        feature.category == Category.CLOUD
        feature.cloud == Cloud.AZURE
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert feature.supports(applicationType)
        }
    }

    void 'test #buildTool object-storage-azure feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["object-storage-azure"])
                .render()

        then:
        template.contains('implementation("io.micronaut.objectstorage:micronaut-object-storage-azure")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    void 'test maven object-storage-azure feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(["object-storage-azure"])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.objectstorage</groupId>
      <artifactId>micronaut-object-storage-azure</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
