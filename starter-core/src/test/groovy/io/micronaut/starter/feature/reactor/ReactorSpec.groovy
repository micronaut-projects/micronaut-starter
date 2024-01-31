package io.micronaut.starter.feature.reactor

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class ReactorSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature reactor contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['reactor'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-reactor/snapshot/guide/index.html")
    }

    @Subject
    @Shared
    Reactor reactor = beanContext.getBean(Reactor)

    void "reactor belongs to Reactive category"() {
        expect:
        Category.REACTIVE == reactor.category
    }

    void "reactor title and description are different"() {
        expect:
        reactor.getTitle()
        reactor.getDescription()
        reactor.getTitle() != reactor.getDescription()
    }

    @Unroll("feature reactor works for application type: #applicationType")
    void "feature reactor works for every type of application type"(ApplicationType applicationType) {
        expect:
        reactor.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with #buildTool and feature reactor for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['http-client', 'reactor'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.reactor", "micronaut-reactor", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.reactor", "micronaut-reactor-http-client", Scope.COMPILE)
        verifier.hasDependency("io.micrometer", "context-propagation", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values().toList(), BuildTool.values()].combinations()
    }
}
