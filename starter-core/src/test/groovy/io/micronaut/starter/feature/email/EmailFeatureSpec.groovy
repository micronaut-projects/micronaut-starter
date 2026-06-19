package io.micronaut.starter.feature.email

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Unroll

class EmailFeatureSpec extends ApplicationContextSpec {

    @Shared
    List<Feature> emailFeatures = beanContext.getBeansOfType(EmailFeature)

    @Unroll("#feature category is Email")
    void "Email Features are in the EMAIL category"(String feature) {
        when:
        Optional<Feature> featureOptional = findFeatureByName(feature)

        then:
        featureOptional.isPresent()
        featureOptional.get().getCategory() == "Email"

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
    void "verify email features add dependencies"(Scope scope,
                                                  BuildTool buildTool,
                                                  String groupId,
                                                  String artifactId,
                                                  List<String> features,
                                                  String coordinate) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(features)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, Language.DEFAULT_OPTION, template)

        then:
        verifier.hasDependency(groupId, artifactId, scope)
        if (features.any {it.contains('views-')}) {
            assert verifier.hasDependency(groupId, "micronaut-email-template", scope)
        }

        where:
        scope         | buildTool               | groupId              | artifactId                 | features
        Scope.TEST    | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-mailpit-http-client' | ['email-mailpit-http-client']
        Scope.TEST    | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-mailpit-http-client' | ['email-mailpit-http-client']
        Scope.TEST    | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-mailpit-http-client' | ['email-mailpit-http-client']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-mailtrap'  | ['email-mailtrap']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-mailtrap' | ['email-mailtrap']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-mailtrap' | ['email-mailtrap']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE        | 'io.micronaut.email' | 'micronaut-email-mailtrap' | ['email-mailtrap', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.GRADLE_KOTLIN | 'io.micronaut.email' | 'micronaut-email-mailtrap' | ['email-mailtrap', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-sendgrid' | ['email-sendgrid', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-amazon-ses' | ['email-amazon-ses', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-postmark' | ['email-postmark', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-mailjet'  | ['email-mailjet', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-javamail' | ['email-javamail', 'views-thymeleaf']
        Scope.COMPILE | BuildTool.MAVEN         | 'io.micronaut.email' | 'micronaut-email-mailtrap' | ['email-mailtrap', 'views-thymeleaf']
        coordinate = "${groupId}:${artifactId}"
    }
}
