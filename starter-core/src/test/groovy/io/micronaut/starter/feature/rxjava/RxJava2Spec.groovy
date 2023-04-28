package io.micronaut.starter.feature.rxjava

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

class RxJava2Spec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    RxJava3 rxJavaThree = beanContext.getBean(RxJava3)

    void 'test readme.md with feature rxjava2 contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['rxjava2'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-rxjava2/snapshot/guide/index.html")
    }

    void "rxjava2 belongs to Reactive category"() {
        expect:
        Category.REACTIVE == rxJavaThree.category
    }

    @Unroll("feature rxjava2 works for application type: #applicationType")
    void "feature rxjava2 works for every type of application type"(ApplicationType applicationType) {
        expect:
        rxJavaThree.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with gradle and feature rxjava2 for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['http-client', 'rxjava2'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("io.micronaut.rxjava2", "micronaut-rxjava2", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.rxjava2", "micronaut-rxjava2-http-client", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.rxjava2", "micronaut-rxjava2-http-server-netty", Scope.RUNTIME)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
