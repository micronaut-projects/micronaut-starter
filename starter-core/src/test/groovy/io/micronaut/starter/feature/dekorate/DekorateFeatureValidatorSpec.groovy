package io.micronaut.starter.feature.dekorate

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class DekorateFeatureValidatorSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test feature #feature.name is not supported for Groovy and #buildTool'(Feature feature, BuildTool buildTool){
        when:
        getFeatures([feature.getName()], Language.GROOVY, TestFramework.JUNIT, buildTool)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Dekorate is not supported in Groovy applications")

        where:
        [feature, buildTool] << [
                beanContext.getBeansOfType(AbstractDekorateFeature),
                [BuildTool.MAVEN, BuildTool.GRADLE]].combinations()
    }

    void 'test feature #feature.name is supported for Kotlin and Maven'(Feature feature){
        when:
        getFeatures([feature.getName()], Language.KOTLIN, TestFramework.JUNIT, BuildTool.MAVEN)

        then:
        noExceptionThrown()

        where:
        feature << beanContext.getBeansOfType(AbstractDekorateFeature)
    }

    @Unroll
    void 'test dekorate service feature #feature.name uses default platform feature for #language'(Feature feature, Language language) {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features([feature.getName()])
                .testFramework(TestFramework.JUNIT)
                .render()

        then:
        beanContext.containsBean(DekorateKubernetes)

        where:
        [feature, language] << [beanContext.getBeansOfType(AbstractDekorateServiceFeature),
                                [Language.JAVA, Language.KOTLIN]].combinations()
    }
}
