package io.micronaut.starter.feature.database

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import spock.lang.Shared
import spock.lang.Subject

class DataSpringJdbcSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    @Shared
    @Subject
    DataSpringJdbcFeature dataSpringJdbcFeature = beanContext.getBean(DataSpringJdbcFeature)

    void 'test readme.md with feature data-jdbc contains links to micronaut docs'() {
        when:
        def output = generate(['data-spring-jdbc'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/#spring")
    }

    void "test data spring jdbc belongs to Database category"() {
        expect:
        Category.DATABASE == dataSpringJdbcFeature.category
    }

    void "test data spring jdbc supports application type #appType"(ApplicationType appType) {
        expect:
        dataSpringJdbcFeature.supports(appType)

        where:
        appType << ApplicationType.values()
    }

    void "test data spring jdbc features"() {
        when:
        Features features = getFeatures(['data-spring-jdbc'])

        then:
        features.contains("data-spring-jdbc")
        features.contains("data-jdbc")
    }

    void "test dependencies are present for buildTool #buildTool"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(["data-spring-jdbc"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasDependency("io.micronaut.data", "micronaut-data-jdbc", Scope.COMPILE)
        verifier.hasDependency("io.micronaut.data", "micronaut-data-spring-jdbc", Scope.COMPILE)

        where:
        buildTool << BuildTool.values()
    }

    void "test render config"() {
        when:
        Map<String, String> output = generate([Yaml.NAME, 'data-spring-jdbc'])
        String config = output["src/main/resources/application.yml"]

        then:
        config
        config.contains("transaction-manager: springJdbc")
    }
}
