package io.micronaut.starter.feature.agorapulse.gru

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.CommunityFeatureValidator
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import spock.lang.Requires
import spock.lang.Unroll

class GruHttpSpec extends ApplicationContextSpec {
    void "Gru-http Feature override Feature->getThirdPartyDocumentation"() {
        given:
        List<ApplicationType> supportedApplicationTypes = [ApplicationType.DEFAULT]

        when:
        Optional<Feature> featureOptional = findFeatureByName('agorapulse-gru-http')

        then:
        featureOptional.isPresent()

        when:
        Feature feature = featureOptional.get()

        then:
        feature.getThirdPartyDocumentation()

        and: 'is in the DEV_TOOLS category'
        feature.getCategory() == "Development Tools"

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

    @Requires({ CommunityFeatureValidator.ENABLE_COMMUNITY_FEATURES })
    @Unroll("#buildTool with feature agorapulse-gru-http adds dependency for #buildTool")
    void "verify agorapulse-gru-http feature dependencies"(BuildTool buildTool) {
        given:
        String groupId = 'com.agorapulse'
        String artifactId = 'gru-micronaut'
        List<String> features = ['agorapulse-gru-http']
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
        buildTool << BuildTool.values()
    }
}
