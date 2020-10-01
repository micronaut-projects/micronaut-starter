package io.micronaut.starter.feature.dekorate

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class DekorateSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature dekorate-kubernetes contains links to micronaut docs'() {
        when:
        def output = generate(['dekorate-kubernetes'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle dekorate #feature.name feature with for #language' (Feature feature, Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures([feature.getName()], language), false).render().toString()
        String service = feature.getName().replaceFirst("dekorate-", "")

        then:
        if(language == Language.JAVA) {
            assert template.contains(String.format('annotationProcessor("io.dekorate:%s-annotations:${dekorateVersion}")', service))
        }
        template.contains(String.format('implementation("io.dekorate:%s-annotations:${dekorateVersion}")', service))
        template.contains('implementation("io.micronaut:micronaut-management")')

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateFeature),
                Language.JAVA, Language.KOTLIN].combinations()
    }

    @Unroll
    void 'test maven dekorate #feature.name for #language' (Feature feature, Language language) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(),
                getFeatures([feature.getName()], language, TestFramework.JUNIT, BuildTool.MAVEN), []).render().toString()
        String service = feature.getName().replaceFirst("dekorate-", "")

        then:
        template.contains(String.format("""
    <dependency>
      <groupId>io.dekorate</groupId>
      <artifactId>%s-annotations</artifactId>
      <version>\${dekorate.version}</version>
      <scope>compile</scope>
    </dependency>""", service))

        if (language == Language.KOTLIN) {
            assert template.contains(String.format("""
                <annotationProcessorPath>
                  <groupId>io.dekorate</groupId>
                  <artifactId>%s-annotations</artifactId>
                  <version>\${dekorate.version}</version>
                </annotationProcessorPath>""", service))
        } else {
            assert template.contains(String.format("""
            <path>
              <groupId>io.dekorate</groupId>
              <artifactId>%s-annotations</artifactId>
              <version>\${dekorate.version}</version>
            </path>""", service))
        }


        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateFeature),
                [Language.JAVA, Language.KOTLIN]].combinations()
    }
}
