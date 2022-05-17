package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.function.CloudProvider
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class DynamoDbSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature dynamodb contains links to micronaut docs'() {
        when:
        def output = generate(['dynamodb'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Amazon DynamoDB documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#dynamodb)")
        readme.contains("[https://aws.amazon.com/dynamodb/](https://aws.amazon.com/dynamodb/)")
    }

    void 'test gradle dynamodb feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['dynamodb'])
                .render()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-aws-sdk-v2")')
        template.contains('implementation("software.amazon.awssdk:dynamodb")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    void 'test maven dynamodb feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['dynamodb'])
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
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>dynamodb</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    void "dynamodb feature is in the DATABASE category"() {
        given:
        String feature = 'dynamodb'

        when:
        Optional<Feature> featureOptional = findFeatureByName(feature)

        then:
        featureOptional.isPresent()

        when:
        Feature f = featureOptional.get()

        then:
        f.category == "Database"

        and: 'supports every application type'
        for (ApplicationType applicationType : ApplicationType.values()) {
            assert f.supports(applicationType)
        }

        and: "aws-alexa belongs to cloud AWS"
        f.cloudProvider.isPresent()
        CloudProvider.AWS == f.cloudProvider.get()
    }
}