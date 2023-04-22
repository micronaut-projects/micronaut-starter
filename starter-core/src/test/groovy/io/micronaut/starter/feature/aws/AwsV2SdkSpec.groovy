package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class AwsV2SdkSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature aws-v2-sdk contains links to micronaut docs'() {
        when:
        def output = generate(['aws-v2-sdk'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut AWS SDK 2.x documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/)")
        readme.contains("[https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/welcome.html](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/welcome.html)")
    }

    void "aws-v2-sdk feature is in the CLOUD category"() {
        given:
        String feature = 'aws-v2-sdk'

        when:
        Optional<Feature> featureOptional = findFeatureByName(feature)

        then:
        featureOptional.isPresent()

        when:
        Feature f = featureOptional.get()

        then:
        f.category == "Cloud"

        and: 'supports every application type'
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert f.supports(applicationType)
        }
    }

    void 'test gradle aws-v2-sdk feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['aws-v2-sdk'])
                .render()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-sdk-v2")')

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.valuesGradle()].combinations()
    }

    void 'test maven aws-v2-sdk feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['aws-v2-sdk'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-aws-sdk-v2</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        where:
        language << Language.values().toList()
    }
}