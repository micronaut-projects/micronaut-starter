package io.micronaut.starter.feature.agorapulse.worker

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.PendingFeature

class WorkerSpec extends ApplicationContextSpec {

    void "Worker Feature override Feature->getThirdPartyDocumentation"() {
        given:
        List<ApplicationType> supportedApplicationTypes = [ApplicationType.DEFAULT]

        when:
        Optional<Feature> featureOptional = findFeatureByName('agorapulse-micronaut-worker')

        then:
        featureOptional.present

        when:
        Feature feature = featureOptional.get()

        then:
        feature.thirdPartyDocumentation

        and: 'is in the SCHEDULING category'
        feature.category == "Scheduling"

        and:
        feature.community

        and: 'supports only a ApplicationType.DEFAULT'
        for (ApplicationType type : (ApplicationType.values() - supportedApplicationTypes)) {
            assert !feature.supports(type)
        }
        for (ApplicationType type : supportedApplicationTypes) {
            assert feature.supports(type)
        }
    }

    @PendingFeature(reason = "agora community features do not support Micronaut Framework 4 yet")
    void "#buildTool with feature agorapulse-micronaut-worker adds dependency #groupId:#artifactId for #language"(Language language, BuildTool buildTool, String groupId, String artifactId) {
        given:
        List<String> features = ['agorapulse-micronaut-worker']
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
        language        | buildTool
        Language.JAVA   | BuildTool.GRADLE
        Language.JAVA   | BuildTool.GRADLE_KOTLIN
        Language.JAVA   | BuildTool.MAVEN
        Language.GROOVY | BuildTool.GRADLE
        Language.GROOVY | BuildTool.GRADLE_KOTLIN
        Language.GROOVY | BuildTool.MAVEN
        Language.KOTLIN | BuildTool.GRADLE
        Language.KOTLIN | BuildTool.GRADLE_KOTLIN
        Language.KOTLIN | BuildTool.MAVEN
        groupId = 'com.agorapulse'
        artifactId = 'micronaut-worker'
    }

}
