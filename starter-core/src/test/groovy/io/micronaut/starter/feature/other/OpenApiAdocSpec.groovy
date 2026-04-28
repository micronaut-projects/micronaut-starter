package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class OpenApiAdocSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "openapi-adoc feature includes openapi feature"() {
        when:
        Features features = getFeatures(['openapi-adoc'])

        then:
        features.contains('openapi')
    }

    void "openapi-adoc belongs to API category"() {
        expect:
        Category.API == beanContext.getBean(OpenApiAdoc).category
    }

    @Unroll
    void 'test openapi-adoc with #buildTool for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features(['openapi-adoc'])
                .language(language)
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, TestFramework.JUNIT, template)

        then:
        verifier.hasAnnotationProcessor('io.micronaut.openapi', 'micronaut-openapi-adoc')

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
