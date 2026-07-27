package io.micronaut.starter.feature.cdi

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class OpenDiSpec extends BeanContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    OpenDi feature = beanContext.getBean(OpenDi)

    void 'opendi belongs to AI category'() {
        expect:
        feature.category == Category.AI
    }

    void 'opendi supports application type #applicationType'(ApplicationType applicationType) {
        expect:
        feature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'opendi dependencies #buildTool'(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['opendi'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency('org.eclipse.odi', 'micronaut-odi-processor-cdi', Scope.ANNOTATION_PROCESSOR)
        verifier.hasDependency('org.eclipse.odi', 'micronaut-odi-cdi', Scope.COMPILE)
        verifier.hasDependency('jakarta.enterprise', 'jakarta.enterprise.cdi-api', Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void 'README.md with opendi contains a link to its documentation'() {
        when:
        def output = generate(['opendi'])
        def readme = output['README.md']

        then:
        readme
        readme.contains('https://github.com/eclipse-ee4j/odi')
    }
}
