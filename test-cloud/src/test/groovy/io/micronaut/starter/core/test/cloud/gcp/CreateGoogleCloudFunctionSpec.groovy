package io.micronaut.starter.core.test.cloud.gcp

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.ApplicationTypeCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Ignore
import spock.lang.Requires
import spock.lang.Retry
import spock.lang.Unroll
import spock.util.environment.Jvm

@Requires({Jvm.current.isJava11Compatible()})
@Retry // can fail on CI due to port binding race condition, so retry
class CreateGoogleCloudFunctionSpec extends CommandSpec{
    @Override
    String getTempDirectoryPrefix() {
        "test-gcpfunction"
    }

    @Ignore("""
i.m.http.server.RouteExecutor - Unexpected error occurred: Receiver class io.micronaut.gcp.function.http.GoogleBinderRegistry does not define or inherit an implementation of the resolved method 'java.util.Optional findArgumentBinder(io.micronaut.core.type.Argument)' of interface io.micronaut.http.bind.RequestBinderRegistry.
java.lang.AbstractMethodError: Receiver class io.micronaut.gcp.function.http.GoogleBinderRegistry does not define or inherit an implementation of the resolved method 'java.util.Optional findArgumentBinder(io.micronaut.core.type.Argument)' of interface io.micronaut.http.bind.RequestBinderRegistry.
\tat io.micronaut.web.router.DefaultMethodBasedRouteInfo.resolveArgumentBindersInternal(DefaultMethodBasedRouteInfo.java:130)
\tat io.micronaut.web.router.DefaultMethodBasedRouteInfo.resolveArgumentBinders(DefaultMethodBasedRouteInfo.java:116)
\tat io.micronaut.web.router.AbstractRouteMatch.fulfillBeforeFilters(AbstractRouteMatch.java:294)
\tat io.micronaut.http.server.binding.RequestArgumentSatisfier.fulfillArgumentRequirementsBeforeFilters(RequestArgumentSatisfier.java:57)""")
    @Unroll
    void 'create-#applicationType with features google-cloud-function #lang and #build and test framework: #testFramework'(ApplicationType applicationType,
                                                                                                                           Language lang,
                                                                                                                           BuildTool build,
                                                                                                                           TestFramework testFramework) {
        given:
        List<String> features = ['google-cloud-function']
        generateProject(lang, build, features, applicationType, testFramework)

        when:
        String output = executeBuild(build, "test")

        then:
        output.contains("BUILD SUCCESS")

        where:
        [applicationType, lang, build, testFramework] << ApplicationTypeCombinations.combinations([ApplicationType.DEFAULT])
    }
}
