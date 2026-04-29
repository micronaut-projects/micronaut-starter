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

class ObjectStorageAwsSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "object-storage-aws has the correct properties"() {
        given:
        ObjectStorageAws feature = beanContext.streamOfType(ObjectStorageAws).findFirst().orElse(null)

        expect:
        feature.name == "object-storage-aws"
        feature.title == "Object Storage - AWS"
        feature.description == "Micronaut Object Storage provides a uniform API to create, read and delete objects in the major cloud providers. This feature adds the AWS implementation"
        feature.category == Category.CLOUD
        feature.cloud == Cloud.AWS
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert feature.supports(applicationType)
        }
    }

    void 'test #buildTool object-storage-aws feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["object-storage-aws"])
                .render()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-sdk-v2")')
        template.contains('implementation("io.micronaut.objectstorage:micronaut-object-storage-aws")')

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.valuesGradle()].combinations()
    }

    void 'test maven object-storage-aws feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(["object-storage-aws"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-sdk-v2", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.objectstorage", "micronaut-object-storage-aws", Scope.COMPILE)
        where:
        language << Language.values().toList()
    }
}
