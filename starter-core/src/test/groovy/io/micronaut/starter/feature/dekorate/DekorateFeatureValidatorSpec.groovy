package io.micronaut.starter.feature.dekorate

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.maven.MavenBuild
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class DekorateFeatureValidatorSpec extends BeanContextSpec implements CommandOutputFixture {

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
    void 'test dekorate service feature #feature.name uses default platform feature for #language'(
            Feature feature, Language language) {
        when:
        pom.template(ApplicationType.DEFAULT, buildProject(),
                getFeatures([feature.getName()], language, TestFramework.JUNIT, BuildTool.MAVEN), new MavenBuild()).render().toString()

        then:
        beanContext.containsBean(DekorateKubernetes)

        where:
        [feature, language] << [beanContext.getBeansOfType(AbstractDekorateServiceFeature),
                                [Language.JAVA, Language.KOTLIN]].combinations()
    }
}
