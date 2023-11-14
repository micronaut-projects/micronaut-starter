package io.micronaut.starter.feature.agorapulse.permissions

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.CommunityFeatureValidator
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.PendingFeature
import spock.lang.Requires

class PermissionsSpec extends ApplicationContextSpec {

    void "Permissions Feature override Feature->getThirdPartyDocumentation"() {
        when:
        Optional<Feature> featureOptional = findFeatureByName('agorapulse-micronaut-permissions')

        then:
        featureOptional.present

        when:
        Feature feature = featureOptional.get()

        then:
        feature.thirdPartyDocumentation

        and: 'is in the SECURITY category'
        feature.category == "Security"

        and:
        feature.community

        and: 'supports all types'
        for (ApplicationType type : ApplicationType.values()) {
            assert feature.supports(type)
        }
    }

    @PendingFeature(reason = "agora community features do not support Micronaut Framework 4 yet")
    void "#buildTool with feature micronaut-permissions adds dependency #groupId:#artifactId for #language"(Language language, BuildTool buildTool, String groupId, String artifactId) {
        given:
        List<String> features = ['agorapulse-micronaut-permissions']
        String coordinate = "${groupId}:${artifactId}"

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .language(language)
                .render()

        then:
        if (buildTool.isGradle()) {
            assert template.contains("implementation(\"$coordinate")
        } else if (buildTool == BuildTool.MAVEN) {
            assert template.contains("<artifactId>$artifactId</artifactId>")
            assert template.contains("<groupId>$groupId</groupId>")
        }

        where:
        language        | buildTool                 | groupId                   | artifactId
        Language.JAVA   | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-permissions'
        Language.JAVA   | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-permissions'
        Language.JAVA   | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-permissions'
        Language.GROOVY | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-permissions'
        Language.GROOVY | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-permissions'
        Language.GROOVY | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-permissions'
        Language.KOTLIN | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-permissions'
        Language.KOTLIN | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-permissions'
        Language.KOTLIN | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-permissions'
    }

}
