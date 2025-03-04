package io.micronaut.starter.feature.build.maven

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Options
import spock.lang.Shared
import spock.lang.Subject

class EnforcerPluginSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature kubernetes-informer contains links to docs'() {
        when:
        Map<String, String> output = generate(['kubernetes-informer'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/kubernetes-client/java/wiki")
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-informer")
    }

    @Subject
    @Shared
    EnforcerPlugin feature = beanContext.getBean(EnforcerPlugin)

    void "enforcer belongs to DEV_TOOLS category"() {
        expect:
        Category.DEV_TOOLS == feature.category
    }

    void "enforcer is not visible"() {
        expect:
        !feature.isVisible()
    }

    void "test enforcer supports application type=#appType"(ApplicationType appType) {
        expect:
        feature.supports(MicronautOptions.builder().applicationType(appType).build())

        where:
        appType << ApplicationType.values().toList()
    }

    void "test enforcer only applied for maven"(boolean expected, BuildTool buildTool) {
        expect:
        expected == feature.shouldApply(MicronautOptions.builder().applicationType(ApplicationType.DEFAULT).buildTool(buildTool).build(), [] as Set)

        where:
        expected | buildTool
        true     | BuildTool.MAVEN
        false    | BuildTool.GRADLE
        false    | BuildTool.GRADLE_KOTLIN
    }
}
