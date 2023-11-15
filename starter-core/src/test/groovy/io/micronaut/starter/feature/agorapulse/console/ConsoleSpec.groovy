package io.micronaut.starter.feature.agorapulse.console

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.PendingFeature

class ConsoleSpec extends ApplicationContextSpec {

    void "Micronaut-console Feature override Feature->getThirdPartyDocumentation"() {
        given:
        List<ApplicationType> supportedApplicationTypes = [ApplicationType.DEFAULT]

        when:
        Optional<Feature> featureOptional = findFeatureByName('agorapulse-micronaut-console')

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

    @PendingFeature(reason = "agora community features do not support Micronaut Framework 4 yet")
    void "#buildTool with feature agorapulse-micronaut-console adds dependency #groupId:#artifactId for #language"(Language language, BuildTool buildTool, String groupId, String artifactId) {
        given:
        List<String> features = ['agorapulse-micronaut-console']
        String coordinate = "${groupId}:${artifactId}"

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .language(language)
                .render()

        then:
        if (buildTool.isGradle()) {
            assert template.contains("developmentOnly(\"$coordinate")
        } else if (buildTool == BuildTool.MAVEN) {
            assert template.contains("<artifactId>$artifactId</artifactId>")
            assert template.contains("<groupId>$groupId</groupId>")
        }

        where:
        language        | buildTool                 | groupId                   | artifactId
        Language.JAVA   | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-console'
        Language.JAVA   | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-console'
        Language.JAVA   | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-console'
        Language.JAVA   | BuildTool.GRADLE          | 'org.apache.groovy'     | 'groovy'
        Language.JAVA   | BuildTool.GRADLE_KOTLIN   | 'org.apache.groovy'     | 'groovy'
        Language.JAVA   | BuildTool.MAVEN           | 'org.apache.groovy'     | 'groovy'
        Language.GROOVY | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-console'
        Language.GROOVY | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-console'
        Language.GROOVY | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-console'
        Language.KOTLIN | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-console'
        Language.KOTLIN | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-console'
        Language.KOTLIN | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-console'
        Language.KOTLIN | BuildTool.GRADLE          | 'org.jetbrains.kotlin'    | 'kotlin-scripting-jsr223'
        Language.KOTLIN | BuildTool.GRADLE_KOTLIN   | 'org.jetbrains.kotlin'    | 'kotlin-scripting-jsr223'
        Language.KOTLIN | BuildTool.MAVEN           | 'org.jetbrains.kotlin'    | 'kotlin-scripting-jsr223'
    }

    @PendingFeature(reason = "agora community features do not support Micronaut Framework 4 yet")
    void 'verify agorapulse-micronaut-console configuration'() {
        when:
            GeneratorContext commandContext = buildGeneratorContext(['agorapulse-micronaut-console'])
            int expectedLength = UUID.randomUUID().toString().length()
        then:
            commandContext.configuration.get('console.enabled') == true
            commandContext.configuration.get('console.addresses') == ['/127.0.0.1', '/0:0:0:0:0:0:0:1']
            commandContext.configuration.get('console.header-name') == 'X-Console-Verify'
            commandContext.configuration.get('console.header-value')?.toString()?.length() == expectedLength
    }
}
