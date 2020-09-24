package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.VersionInfo

class MavenSpec extends BeanContextSpec {

    void 'test use defaults from parent pom'() {
        given:
        GeneratorContext generatorContext = buildGeneratorContext([], new Options(null, null, BuildTool.MAVEN))

        when:
        String template = pom.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                generatorContext.getBuildProperties().getProperties()
        ).render().toString()

        then: 'parent pom is used'
        template.contains("""
  <parent>
    <groupId>io.micronaut</groupId>
    <artifactId>micronaut-parent</artifactId>
    <version>${VersionInfo.micronautVersion}</version>
  </parent>
""")
        then:'shade plugin applies to maven by default'
        template.contains("""
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
      </plugin>
""")

        then: 'there are no Maven specific properties defined, all of them are inherited from parent pom'
        !template.contains('<maven.compiler.target>')
        !template.contains('<maven.compiler.source>')
        !template.contains('<project.build.sourceEncoding>')
        !template.contains('<project.reporting.outputEncoding>')
        !template.contains('<maven-surefire-plugin.version>')
        !template.contains('<maven-failsafe-plugin.version>')

        then: 'Surefire is inherited from parent pom so it should not appear in generated pom'
        !template.contains('<artifactId>maven-surefire-plugin</artifactId>')

        then: 'Failsafe is declared but without any specific configuration (configuration is inherited from parent pom)'
        template.contains('''
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
      </plugin>
''')
    }

    void 'test annotation processor dependencies'() {
        when:
        Features features = getFeatures([], null, null, BuildTool.MAVEN)
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

        then:
        template.contains('''
          <annotationProcessorPaths>
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-inject-java</artifactId>
              <version>${micronaut.version}</version>
            </path>
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-validation</artifactId>
              <version>${micronaut.version}</version>
            </path>
          </annotationProcessorPaths>
''')

        when:
        features = getFeatures([], Language.KOTLIN, null, BuildTool.MAVEN)
        template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

        then:
        template.contains("""
              <annotationProcessorPaths>
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-inject-java</artifactId>
                  <version>\${micronaut.version}</version>
                </annotationProcessorPath>
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-validation</artifactId>
                  <version>\${micronaut.version}</version>
                </annotationProcessorPath>
              </annotationProcessorPaths>
""")

        when:
        features = getFeatures([], Language.GROOVY, null, BuildTool.MAVEN)
        template = pom.template(ApplicationType.DEFAULT, buildProject(), features, []).render().toString()

        then:
        template.contains('''
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-inject</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-validation</artifactId>
      <scope>compile</scope>
    </dependency>
''')
    }

}
