package io.micronaut.starter.feature.function.azure

import io.micronaut.starter.options.JdkVersion
import spock.lang.Specification

class AzureFunctionFeatureValidatorTest extends Specification {
    void "max supported version for azure is 21"() {
        expect:
        JdkVersion.JDK_21 == AzureFunctionFeatureValidator.getMaxJdkSupportedVersion()
    }
}
