package io.micronaut.starter.feature.objectstorage

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
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

    void "ObjecStorageFeature is OneOfFeature"() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(Language.JAVA)
                .features(["object-storage-aws", "object-storage-gcp"])
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message.contains('There can only be one of the following features selected: [object-storage-gcp, object-storage-aws]')
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
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    void 'test maven object-storage-aws feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(["object-storage-aws"])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-sdk-v2</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.objectstorage</groupId>
      <artifactId>micronaut-object-storage-aws</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }
}
