package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.feature.graalvm.GraalVMFeatureValidator
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class DynamoDbSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature dynamodb contains links to micronaut docs'() {
        when:
        def output = generate([DynamoDb.NAME])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Amazon DynamoDB documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#dynamodb)")
        readme.contains("[https://aws.amazon.com/dynamodb/](https://aws.amazon.com/dynamodb/)")
    }

    void 'test #buildTool dynamodb feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([DynamoDb.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-sdk-v2", Scope.COMPILE)
        verifier.hasDependency("software.amazon.awssdk", "dynamodb", Scope.COMPILE)

        and: 'validation feature is applied since the rocker templates of dynamodb use annotations'
        verifier.hasDependency("io.micronaut.validation", "micronaut-validation", Scope.COMPILE)
        verifier.hasAnnotationProcessor("io.micronaut.validation", "micronaut-validation-processor")

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
    
    void 'test #buildTool dynamodb feature for language=#language with GraalVM'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([DynamoDb.NAME, GraalVM.FEATURE_NAME_GRAALVM])
                .render()
        String mapNotation = buildTool == BuildTool.GRADLE ? ':' : ' ='
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-sdk-v2", Scope.COMPILE)
        verifier.hasExclusion("software.amazon.awssdk", "dynamodb", "software.amazon.awssdk", "apache-client")
        verifier.hasExclusion("software.amazon.awssdk", "dynamodb", "software.amazon.awssdk", "netty-nio-client")
        verifier.hasDependency("software.amazon.awssdk", "url-connection-client", Scope.COMPILE)

        where:
        [language, buildTool] << [GraalVMFeatureValidator.supportedLanguages(), [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    void 'test maven dynamodb feature for language=#language with GraalVM'(Language language) {
        when:
        BuildTool buildTool = BuildTool.MAVEN
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([DynamoDb.NAME, GraalVM.FEATURE_NAME_GRAALVM])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-sdk-v2", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.aws", "micronaut-aws-sdk-v2", Scope.COMPILE)
        verifier.hasDependency("software.amazon.awssdk", "url-connection-client", Scope.COMPILE)

        verifier.hasDependency("software.amazon.awssdk", "dynamodb", Scope.COMPILE)
        verifier.hasExclusion("software.amazon.awssdk", "dynamodb", "software.amazon.awssdk", "apache-client")
        verifier.hasExclusion("software.amazon.awssdk", "dynamodb", "software.amazon.awssdk", "netty-nio-client")

        where:
        language << GraalVMFeatureValidator.supportedLanguages()
    }

    void "dynamodb feature is in the DATABASE category"() {
        given:
        String feature = DynamoDb.NAME

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
    }
}