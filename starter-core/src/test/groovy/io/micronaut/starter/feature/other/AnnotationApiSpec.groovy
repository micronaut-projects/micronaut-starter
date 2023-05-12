package io.micronaut.starter.feature.other

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.DefaultFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Subject
import spock.lang.Unroll

class AnnotationApiSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AnnotationApi annotationApi = beanContext.getBean(AnnotationApi)

    void "annotation api is not a default feature"() {
        expect:
        !(annotationApi instanceof DefaultFeature)
    }

    @Unroll
    void 'test #buildTool views-velocity feature for language=#language'(Language language, BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(['annotation-api'])
                .render()

        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, template)

        then:
        verifier.hasDependency("jakarta.annotation", "jakarta.annotation-api", Scope.COMPILE)

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }
}
