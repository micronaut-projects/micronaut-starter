package io.micronaut.starter.feature.email

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Unroll

class EmailFeatureSpec extends ApplicationContextSpec {

    @Shared
    List<Feature> emailFeatures = beanContext.getBeansOfType(EmailFeature)

    @Unroll("#feature category is MESSAGING")
    void "Email Features are in the MESSAGING category"(String feature) {
        when:
        Optional<Feature> featureOptional = findFeatureByName(feature)

        then:
        featureOptional.isPresent()
        featureOptional.get().getCategory() == "Messaging"

        where:
        feature << emailFeatures*.name + ['email-template']
    }

    @Unroll("#feature overrides Feature->getThirdPartyDocumentation")
    void "Email Features override Feature->getThirdPartyDocumentation"(String feature) {
        when:
        Optional<Feature> featureOptional = findFeatureByName(feature)

        then:
        featureOptional.isPresent()
        featureOptional.get().getThirdPartyDocumentation()

        where:
        feature << emailFeatures*.name
    }

    @Unroll("#feature overrides Feature->getMicronautDocumentation")
    void "Email Features override Feature->getMicronautDocumentation"(String feature) {
        when:
        Optional<Feature> featureOptional = findFeatureByName(feature)

        then:
        featureOptional.isPresent()
        featureOptional.get().getMicronautDocumentation()

        where:
        feature << emailFeatures*.name
    }

    @Unroll("#buildTool with feature #features adds #coordinate dependency")
    void "verify email features add dependencies"(BuildTool buildTool,
                                                  String groupId,
                                                  String artifactId,
                                                  List<String> features,
                                                  String coordinate) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .render()

        then:
        if (buildTool.isGradle()) {
            assert template.contains("implementation(\"$coordinate")
        } else if (buildTool == BuildTool.MAVEN) {
            assert template.contains("<artifactId>$artifactId</artifactId>")
            assert template.contains("<groupId>$groupId</groupId>")
        }
        if (features.any {it.contains('views-')}) {
            if (buildTool.isGradle()) {
                assert template.contains("implementation(\"${groupId}:micronaut-email-template\")")
            } else if (buildTool == BuildTool.MAVEN) {
                assert template.contains("<artifactId>micronaut-email-template</artifactId>")
            }
        }

        where:
        buildTool               | groupId              | artifactId                 | features
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid', 'views-thymeleaf']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses', 'views-thymeleaf']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark', 'views-thymeleaf']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet', 'views-thymeleaf']
        BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail', 'views-thymeleaf']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid', 'views-thymeleaf']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses', 'views-thymeleaf']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark', 'views-thymeleaf']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet', 'views-thymeleaf']
        BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail', 'views-thymeleaf']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid', 'views-thymeleaf']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses', 'views-thymeleaf']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark', 'views-thymeleaf']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet', 'views-thymeleaf']
        BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail', 'views-thymeleaf']
        coordinate = "${groupId}:${artifactId}"
    }
}
