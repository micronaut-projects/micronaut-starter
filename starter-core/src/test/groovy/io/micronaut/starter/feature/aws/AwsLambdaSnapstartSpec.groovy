package io.micronaut.starter.feature.aws

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.feature.architecture.CpuArchitecture
import io.micronaut.starter.feature.architecture.X86
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class AwsLambdaSnapstartSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    AwsLambdaSnapstart snapstart = beanContext.getBean(AwsLambdaSnapstart)

    void "is not visible"() {
        expect:
        !snapstart.isVisible()
    }

    @Unroll
    void "snapstart does not support #description application type"(ApplicationType applicationType, String description) {
        expect:
        !snapstart.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - supportedApplicationTypesForSnapStart())
        description = applicationType.name
    }

    void "snapstart overrides Feature->getThirdPartyDocumentation"() {
        expect:
        snapstart.thirdPartyDocumentation
    }

    @Unroll
    void "snapstart supports #description application type"(ApplicationType applicationType, String description) {
        expect:
        snapstart.supports(applicationType)

        where:
        applicationType << supportedApplicationTypesForSnapStart()
        description = applicationType.name
    }

    @Unroll
    void "snapstart supports #description cpu architecture"(CpuArchitecture architecture, String description) {
        expect:
        snapstart.supports(architecture)

        where:
        architecture << [new X86()]
        description = architecture.name
    }

    @Unroll
    void "snapstart does not support #description cpu architecture"(CpuArchitecture architecture, String description) {
        expect:
        !snapstart.supports(architecture)

        where:
        architecture << [new Arm()]
        description = architecture.name
    }

    private static List<ApplicationType> supportedApplicationTypesForSnapStart() {
        [ApplicationType.DEFAULT, ApplicationType.FUNCTION]
    }

}
