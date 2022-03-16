package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.dependencies.DefaultCoordinateResolver
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.options.*
import spock.lang.Specification
import spock.lang.Unroll

class AzureFeatureValidatorSpec extends Specification {

    @Unroll
    void "azure http and raw features support #javaVersion for #feature"(JdkVersion javaVersion, Feature feature) {
        given:
        AzureFeatureValidator validator = new AzureFeatureValidator()
        Options options = new Options(Language.JAVA,
                TestFramework.JUNIT, BuildTool.GRADLE, javaVersion)

        Set<Feature> features = []
        features << feature

        when:
        validator.validatePostProcessing(options, ApplicationType.DEFAULT, features)

        then:
        noExceptionThrown()

        where:
        [javaVersion, feature] << [
                supportedVersionsByAzureFunction(),
                [new AzureHttpFunction(new DefaultCoordinateResolver()), new AzureRawFunction(new DefaultCoordinateResolver(), new AzureHttpFunction(new DefaultCoordinateResolver()))]
        ].combinations()
    }

    static List<JdkVersion> supportedVersionsByAzureFunction() {
        [JdkVersion.JDK_8, JdkVersion.JDK_11]
    }
}
