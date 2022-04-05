package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.maven.MavenBuild
import io.micronaut.starter.build.maven.MavenCombineAttribute
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import io.micronaut.starter.util.VersionInfo
import spock.lang.Issue
import spock.lang.PendingFeature
import spock.lang.Unroll

class MavenSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test use defaults from parent pom'() {
        given:
        GeneratorContext generatorContext = buildGeneratorContext([], new Options(null, null, BuildTool.MAVEN))

        when:
        String template = pom.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                new MavenBuild([],[], [], generatorContext.getBuildProperties().getProperties(), [], MavenCombineAttribute.APPEND, MavenCombineAttribute.APPEND),
        ).render().toString()

        then: 'parent pom is used'
        template.contains("""
  <parent>
    <groupId>io.micronaut</groupId>
    <artifactId>micronaut-parent</artifactId>
    <version>${VersionInfo.micronautVersion}</version>
  </parent>
""")

        and: 'there are no Maven specific properties defined, all of them are inherited from parent pom'
        !template.contains('<maven.compiler.target>')
        !template.contains('<maven.compiler.source>')
        !template.contains('<project.build.sourceEncoding>')
        !template.contains('<project.reporting.outputEncoding>')
        !template.contains('<maven-surefire-plugin.version>')
        !template.contains('<maven-failsafe-plugin.version>')

        and: 'Surefire is inherited from parent pom so it should not appear in generated pom'
        !template.contains('<artifactId>maven-surefire-plugin</artifactId>')

        and: 'Failsafe is inherited'
        !template.contains('''
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
      </plugin>
''')
        and: 'it contains chidren-specific properties'
        template.contains('<packaging>jar</packaging>')
        template.contains('<micronaut.runtime>')
    }

    void 'test annotation processor dependencies'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN).render()

        then:
        template.contains('''
          <annotationProcessorPaths combine.children="append">
''')

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.KOTLIN)
                .render()

        then:
        !template.contains('''\
              <annotationProcessorPaths combine.children="append">
               <annotationProcessorPath>
                 <groupId>io.micronaut</groupId>
                 <artifactId>micronaut-inject-java</artifactId>
                 <version>${micronaut.version}</version>
               </annotationProcessorPath>
               <annotationProcessorPath>
                 <groupId>io.micronaut</groupId>
                 <artifactId>micronaut-validation</artifactId>
                 <version>${micronaut.version}</version>
               </annotationProcessorPath>
              </annotationProcessorPaths>
''')

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .render()

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

    @Unroll
    void 'test micronaut runtime for #chosenFeatures'(ApplicationType applicationType, List<String> chosenFeatures, String runtime) {
        given:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11)
        Features features = getFeatures(chosenFeatures, options, applicationType)
        println features.features

        when:
        String template = pom.template(applicationType, buildProject(), features, new MavenBuild()).render().toString()

        then:
        template.contains("<micronaut.runtime>${runtime}</micronaut.runtime>")

        where:
        applicationType             | chosenFeatures                       | runtime
        ApplicationType.DEFAULT     | ["google-cloud-function"]       | "google_function"
        ApplicationType.DEFAULT     | ["oracle-function"]             | "oracle_function"
        ApplicationType.DEFAULT     | ["azure-function"]              | "azure_function"
        ApplicationType.DEFAULT     | ["aws-lambda"]                  | "lambda_java"
        ApplicationType.DEFAULT     | ["aws-lambda", 'graalvm']       | "lambda_provided"
        ApplicationType.DEFAULT     | ["tomcat-server"]               | "tomcat"
        ApplicationType.DEFAULT     | ["jetty-server"]                | "jetty"
        ApplicationType.DEFAULT     | ["netty-server"]                | "netty"
        ApplicationType.DEFAULT     | ["undertow-server"]             | "undertow"
    }

}
