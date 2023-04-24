package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool

class HttpClientSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature http-client contains links to micronaut docs'() {
        when:
        def output = generate(['http-client'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#nettyHttpClient")
    }

    void "dependency added for http-client feature in the main classpath"(BuildTool buildTool, List<String> features) {
        when:
        String template = new BuildBuilder(beanContext, buildTool).features(features).render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.TEST)

        where:
        [buildTool, features] << combinations()
    }

    void "dependency added for http-client feature in the test classpath for function"(BuildTool buildTool, List<String> features) {
        when:
        String template = new BuildBuilder(beanContext, buildTool).applicationType(ApplicationType.FUNCTION).features(features).render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE)
        verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.TEST)

        where:
        [buildTool, features] << combinations()
    }

    private static List combinations() {
        [
                BuildTool.values(),
                [
                        [HttpClient.NAME],
                        [] // http-client is a default feature
                ]
        ].combinations()
    }
}
