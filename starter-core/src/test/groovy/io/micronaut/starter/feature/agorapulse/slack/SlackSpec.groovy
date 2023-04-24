package io.micronaut.starter.feature.agorapulse.slack

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.CommunityFeatureValidator
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Requires

@Requires({ CommunityFeatureValidator.ENABLE_COMMUNITY_FEATURES })
class SlackSpec extends ApplicationContextSpec {

    void "Slack Feature override Feature->getThirdPartyDocumentation"() {
        when:
        Optional<Feature> featureOptional = findFeatureByName('agorapulse-micronaut-slack')

        then:
        featureOptional.present

        when:
        Feature feature = featureOptional.get()

        then:
        feature.thirdPartyDocumentation

        and: 'is in the CLIENT category'
        feature.category == 'Client'

        and:
        feature.community

        and: 'supports default type'
        assert feature.supports(ApplicationType.DEFAULT)
    }

    void "#buildTool with feature micronaut-slack adds dependency #groupId:#artifactId for #language"(Language language, BuildTool buildTool, String groupId, String artifactId) {
        given:
        List<String> features = ['agorapulse-micronaut-slack']
        String coordinate = "${groupId}:${artifactId}"

        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .language(language)
                .render()

        then:
        if (buildTool.gradle) {
            assert template.contains("implementation(\"$coordinate")
        } else if (buildTool == BuildTool.MAVEN) {
            assert template.contains("<artifactId>$artifactId</artifactId>")
            assert template.contains("<groupId>$groupId</groupId>")
        }

        where:
        language        | buildTool                 | groupId                   | artifactId
        Language.JAVA   | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.JAVA   | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.JAVA   | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.GROOVY | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.GROOVY | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.GROOVY | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.KOTLIN | BuildTool.GRADLE          | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.KOTLIN | BuildTool.GRADLE_KOTLIN   | 'com.agorapulse'          | 'micronaut-slack-http'
        Language.KOTLIN | BuildTool.MAVEN           | 'com.agorapulse'          | 'micronaut-slack-http'
    }

    void 'verify micronaut-slack configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['agorapulse-micronaut-slack'])
        then:
        commandContext.configuration.get('slack.bot-token')
        commandContext.configuration.get('slack.bot-token').toString().startsWith('xoxb-')
    }

    void 'verify micronaut-slack configuration with caffeine'() {
        when:
            GeneratorContext commandContext = buildGeneratorContext(['agorapulse-micronaut-slack', 'cache-caffeine'])
        then:
            commandContext.configuration.get('micronaut.caches.slack-events.expire-after-write') == '10m'
    }

    void 'verify micronaut-slack configuration with redis'() {
        when:
            GeneratorContext commandContext = buildGeneratorContext(['agorapulse-micronaut-slack', 'redis-lettuce'])
        then:
            commandContext.configuration.get('redis.caches.slack-events.expire-after-write') == '10m'
    }

    void 'verify micronaut-slack configuration with ehcache'() {
        when:
            GeneratorContext commandContext = buildGeneratorContext(['agorapulse-micronaut-slack', 'cache-ehcache'])
        then:
            commandContext.configuration.get('ehcache.caches.slack-events.enabled') == 'true'
    }

}
