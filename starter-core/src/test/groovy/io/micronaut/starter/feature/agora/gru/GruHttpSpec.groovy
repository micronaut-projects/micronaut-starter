package io.micronaut.starter.feature.agora.gru

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import spock.lang.Unroll

class GruHttpSpec extends ApplicationContextSpec {
    void "Gru-http Feature override Feature->getThirdPartyDocumentation"() {
        given:
        List<ApplicationType> supportedApplicationTypes = [ApplicationType.DEFAULT]

        when:
        Optional<Feature> featureOptional = findFeatureByName('gru-http')

        then:
        featureOptional.isPresent()

        when:
        Feature feature = featureOptional.get()

        then:
        feature.getThirdPartyDocumentation()

        and: 'is in the DEV_TOOLS category'
        feature.getCategory() == "Development Tools"

        and: 'supports only a ApplicationType.DEFAULT'
        for (ApplicationType type : (ApplicationType.values() - supportedApplicationTypes)) {
            assert !feature.supports(type)
        }
        for (ApplicationType type : supportedApplicationTypes) {
            assert feature.supports(type)
        }
    }

    @Unroll("#buildTool with feature gru-http adds dependency for #buildTool")
    void "verify gru-http feature dependencies"(BuildTool buildTool) {
        given:
        String groupId = 'com.agorapulse'
        String artifactId = 'gru-micronaut'
        List<String> features = ['gru-http']
        String coordinate = "${groupId}:${artifactId}"

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .render()

        then:
        if (buildTool.isGradle()) {
            assert template.contains("testImplementation(\"$coordinate")
        } else if (buildTool == BuildTool.MAVEN) {
            assert template.contains("<artifactId>$artifactId</artifactId>")
            assert template.contains("<groupId>$groupId</groupId>")
        }

        where:
        buildTool << [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN, BuildTool.MAVEN]
    }
}
