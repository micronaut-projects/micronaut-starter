package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class AwaitilitySpec extends ApplicationContextSpec {

    void "Awaitlity Feature override Feature->getThirdPartyDocumentation"() {
        when:
        Optional<Feature> featureOptional = findFeatureByName('awaitility')

        then:
        featureOptional.present

        when:
        Feature feature = featureOptional.get()

        then:
        feature.thirdPartyDocumentation

        and: 'is in the DEV TOOLS category'
        feature.category == Category.DEV_TOOLS

        and: 'supports every ApplicationType'
        for (ApplicationType type : ApplicationType.values()) {
            assert feature.supports(type)
        }
    }

    @Unroll("#buildTool with feature awaitility adds dependency #groupId:#artifactId for #language")
    void "verify awaitility feature dependencies"(Language language, BuildTool buildTool, String groupId, String artifactId) {
        given:
        List<String> features = ['awaitility']
        String coordinate = "${groupId}:${artifactId}"

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .language(language)
                .render()

        then:
        if (buildTool.isGradle()) {
            assert template.contains("testImplementation(\"$coordinate")
        } else if (buildTool == BuildTool.MAVEN) {
            assert template.contains("<artifactId>$artifactId</artifactId>")
            assert template.contains("<groupId>$groupId</groupId>")
        }

        where:
        language        | buildTool                 | groupId                   | artifactId
        Language.JAVA   | BuildTool.GRADLE          | 'org.awaitility'          | 'awaitility'
        Language.JAVA   | BuildTool.GRADLE_KOTLIN   | 'org.awaitility'          | 'awaitility'
        Language.JAVA   | BuildTool.MAVEN           | 'org.awaitility'          | 'awaitility'
        Language.GROOVY | BuildTool.GRADLE          | 'org.awaitility'          | 'awaitility-groovy'
        Language.GROOVY | BuildTool.GRADLE_KOTLIN   | 'org.awaitility'          | 'awaitility-groovy'
        Language.GROOVY | BuildTool.MAVEN           | 'org.awaitility'          | 'awaitility-groovy'
        Language.KOTLIN | BuildTool.GRADLE          | 'org.awaitility'          | 'awaitility-kotlin'
        Language.KOTLIN | BuildTool.GRADLE_KOTLIN   | 'org.awaitility'          | 'awaitility-kotlin'
        Language.KOTLIN | BuildTool.MAVEN           | 'org.awaitility'          | 'awaitility-kotlin'
    }

}
