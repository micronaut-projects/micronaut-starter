package io.micronaut.starter.feature.dekorate

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class DekorateSpec extends ApplicationContextSpec implements CommandOutputFixture {

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
        given:
        TestFramework testFramework = TestFramework.SPOCK
        Project project = buildProject()
        BuildTool buildTool = BuildTool.GRADLE
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures([feature.getName()], language, testFramework)
        Options options = new Options(language, testFramework, buildTool)
        GradleBuild build = gradleBuild(options, features, project, type)
        String template = buildGradle.template(type, project, features, build).render().toString()

        then:
        template.contains('implementation("io.micronaut:micronaut-management")')
        if(language == Language.JAVA) {
            assert template.contains(String.format('annotationProcessor("io.dekorate:%s-annotations:', service))
        }
        template.contains(String.format('implementation("io.dekorate:%s-annotations:', service))

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseDependencySemanticVersion(template, String.format('io.dekorate:%s-annotations', service))

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateFeature),
                [Language.JAVA, Language.KOTLIN]
        ].combinations()

        service = feature.getName().replaceFirst("dekorate-", "")
    }

    @Unroll
    void 'test maven dekorate #feature.name for #language' (Feature feature, Language language) {

        given:
        TestFramework testFramework = TestFramework.JUNIT
        Project project = buildProject()
        BuildTool buildTool = BuildTool.MAVEN
        ApplicationType type = ApplicationType.DEFAULT

        when:
        Features features = getFeatures([feature.getName()], language, testFramework)
        Options options = new Options(language, testFramework, buildTool)
        MavenBuild build = mavenBuild(options, features, project, type)
        String template = pom.template(type, project, features, build).render().toString()

        then:
        template.contains("<dekorate.version>")
        template.contains("</dekorate.version>")
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

        when:
        Optional<SemanticVersion> semanticVersionOptional = parsePropertySemanticVersion(template, "dekorate.version")

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        [feature, language] << [
                beanContext.getBeansOfType(AbstractDekorateFeature),
                [Language.JAVA, Language.KOTLIN],
        ].combinations()

        service = feature.getName().replaceFirst("dekorate-", "")
    }
}
