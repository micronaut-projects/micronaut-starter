package io.micronaut.starter.feature.agorapulse.worker

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class WorkerSpec extends ApplicationContextSpec {

    void "Worker Feature override Feature->getThirdPartyDocumentation"() {
        given:
            List<ApplicationType> supportedApplicationTypes = [ApplicationType.DEFAULT]

        when:
            Optional<Feature> featureOptional = findFeatureByName('micronaut-worker')

        then:
            featureOptional.isPresent()

        when:
            Feature feature = featureOptional.get()

        then:
            feature.getThirdPartyDocumentation()

        and: 'is in the OTHER category'
            feature.getCategory() == "Other"

        and:
            feature.isCommunity()

        and: 'supports only a ApplicationType.DEFAULT'
            for (ApplicationType type : (ApplicationType.values() - supportedApplicationTypes)) {
                assert !feature.supports(type)
            }
            for (ApplicationType type : supportedApplicationTypes) {
                assert feature.supports(type)
            }
    }

    @Unroll("#buildTool with feature micronaut-worker adds dependency #groupId:#artifactId for #language")
    void "verify micronaut-worker feature dependencies"(Language language, BuildTool buildTool, String groupId, String artifactId) {
        given:
            List<String> features = ['micronaut-worker']
            String coordinate = "${groupId}:${artifactId}"

        when:
            String template = new BuildBuilder(beanContext, buildTool)
                    .features(features)
                    .language(language)
                    .render()

        then:
            if (buildTool.isGradle()) {
                assert template.contains("$configuration(\"$coordinate")
            } else if (buildTool == BuildTool.MAVEN) {
                assert template.contains("<artifactId>$artifactId</artifactId>")
                assert template.contains("<groupId>$groupId</groupId>")
            }

        where:
            language        | buildTool                 | configuration         | groupId                   | artifactId
            Language.JAVA   | BuildTool.GRADLE          | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.JAVA   | BuildTool.GRADLE_KOTLIN   | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.JAVA   | BuildTool.MAVEN           | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.GROOVY | BuildTool.GRADLE          | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.GROOVY | BuildTool.GRADLE_KOTLIN   | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.GROOVY | BuildTool.MAVEN           | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.KOTLIN | BuildTool.GRADLE          | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.KOTLIN | BuildTool.GRADLE_KOTLIN   | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.KOTLIN | BuildTool.MAVEN           | 'implementation'      | 'com.agorapulse'          | 'micronaut-worker'
            Language.JAVA   | BuildTool.GRADLE          | 'testImplementation'  | 'org.awaitility'          | 'awaitility'
            Language.JAVA   | BuildTool.GRADLE_KOTLIN   | 'testImplementation'  | 'org.awaitility'          | 'awaitility'
            Language.JAVA   | BuildTool.MAVEN           | 'testImplementation'  | 'org.awaitility'          | 'awaitility'
            Language.KOTLIN | BuildTool.GRADLE          | 'testImplementation'  | 'org.awaitility'          | 'awaitility'
            Language.KOTLIN | BuildTool.GRADLE_KOTLIN   | 'testImplementation'  | 'org.awaitility'          | 'awaitility'
            Language.KOTLIN | BuildTool.MAVEN           | 'testImplementation'  | 'org.awaitility'          | 'awaitility'
    }

}
