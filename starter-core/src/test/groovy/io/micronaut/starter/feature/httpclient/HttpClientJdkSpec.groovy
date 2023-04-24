package io.micronaut.starter.feature.httpclient

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Subject

class HttpClientJdkSpec extends BeanContextSpec  implements CommandOutputFixture {
    @Subject
    HttpClientJdk httpClientJdk = beanContext.getBean(HttpClientJdk)

    void 'test readme.md with feature http-client-jdk contains links to micronaut and 3rd party docs'() {
        when:
        def output = generate(['http-client-jdk'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#jdkHttpClient")
        readme.contains("https://openjdk.org/groups/net/httpclient/intro.html")
    }

    void "dependency added for http-client-jdk feature for buildTool=#buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['http-client-jdk'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.TEST)

        where:
        buildTool << BuildTool.values().toList()
    }

    void "http-client-jdk does not support applicationType FUNCTION"() {
        expect:
        !httpClientJdk.supports(ApplicationType.FUNCTION)
    }

    void "http-client-jdk supports #applicationType application type"(ApplicationType applicationType) {
        expect:
        httpClientJdk.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION)
    }
}
