package io.micronaut.starter.feature.dekorate

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class DekorateSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature dekorate-kubernetes contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['dekorate-kubernetes'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html")
    }

    @Unroll
    void 'test gradle dekorate #feature.name feature with for #language' (Feature feature, Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features([feature.getName()])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut:micronaut-management")')
        if(language == Language.JAVA) {
            assert template.contains(String.format('annotationProcessor("io.dekorate:%s-annotations")', service))
        }
        template.contains(String.format('implementation("io.dekorate:%s-annotations")', service))

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateFeature),
                [Language.JAVA, Language.KOTLIN]
        ].combinations()

        service = feature.getName().replaceFirst("dekorate-", "")
    }

    @Unroll
    void 'test maven dekorate #feature.name for #language' (Feature feature, Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([feature.getName()])
                .language(language)
                .render()

        then:
        !template.contains("<dekorate.version>")
        !template.contains("</dekorate.version>")
        template.contains(String.format("""
    <dependency>
      <groupId>io.dekorate</groupId>
      <artifactId>%s-annotations</artifactId>
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

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "dekorate.version")

        then:
        noExceptionThrown()
        !semanticVersionOptional.isPresent()

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateFeature),
                [Language.JAVA, Language.KOTLIN],
        ].combinations()

        service = feature.getName().replaceFirst("dekorate-", "")
    }
}
