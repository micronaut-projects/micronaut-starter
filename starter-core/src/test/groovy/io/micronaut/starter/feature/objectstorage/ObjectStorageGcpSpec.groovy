package io.micronaut.starter.feature.objectstorage

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class ObjectStorageGcpSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "object-storage-gcp has the correct properties"() {
        given:
        ObjectStorageGcp feature = beanContext.streamOfType(ObjectStorageGcp).findFirst().orElse(null)

        expect:
        feature.name == "object-storage-gcp"
        feature.title == "Object Storage - GCP"
        feature.description == "Micronaut Object Storage provides a uniform API to create, read and delete objects in the major cloud providers. This feature adds the GCP implementation"
        feature.category == Category.CLOUD
        feature.cloud == Cloud.GCP
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert feature.supports(applicationType)
        }
    }

    void 'test #buildTool object-storage-gcp feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["object-storage-gcp"])
                .render()

        then:
        template.contains('implementation("io.micronaut.objectstorage:micronaut-object-storage-gcp")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    void 'test maven object-storage-gcp feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(["object-storage-gcp"])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.objectstorage</groupId>
      <artifactId>micronaut-object-storage-gcp</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
