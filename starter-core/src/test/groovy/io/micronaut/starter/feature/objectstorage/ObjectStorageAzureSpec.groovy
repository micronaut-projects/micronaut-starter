package io.micronaut.starter.feature.objectstorage

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
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
        [language, buildTool] << [Language.values().toList(), BuildTool.valuesGradle()].combinations()
    }

    void 'test maven object-storage-azure feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(["object-storage-azure"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.objectstorage", "micronaut-object-storage-azure", Scope.COMPILE)
        where:
        language << supportedLanguages(BuildTool.MAVEN)
    }
}
