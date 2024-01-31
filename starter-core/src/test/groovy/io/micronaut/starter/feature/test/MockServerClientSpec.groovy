package io.micronaut.starter.feature.test

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.database.TestContainers
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class MockServerClientSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    MockServerClient mockServerClient = beanContext.getBean(MockServerClient)

    void 'test readme.md with feature MockServerClient contains links to 3rd party docs'() {
        when:
        Map<String, String> output = generate([MockServerClient.NAME])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://www.mock-server.com/mock_server/mockserver_clients.html#java-mockserver-client")
    }

    void "test MockServerClient belongs to Dev Tools category"() {
        expect:
        Category.DEV_TOOLS == mockServerClient.category
    }

    void 'test MockServerClient feature for language=#language and buildTool=#buildTool'() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features([MockServerClient.NAME] + (hasTC ? [TestContainers.NAME] : []))
                .testFramework(TestFramework.JUNIT)
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("org.testcontainers", "mockserver", Scope.TEST) == hasTC
        verifier.hasDependency("org.mock-server", "mockserver-client-java", Scope.TEST)

        where:
        [language, hasTC, buildTool] << [Language.values().toList(), [true, false], BuildTool.values().toList()].combinations()
    }
}
