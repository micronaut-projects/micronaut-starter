package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.Features
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.feature.function.azure.AzureHttpFunction
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction
import io.micronaut.starter.feature.function.oraclefunction.OracleFunction
import io.micronaut.starter.feature.server.Jetty
import io.micronaut.starter.feature.server.Netty
import io.micronaut.starter.feature.server.Tomcat
import io.micronaut.starter.feature.server.Undertow
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import spock.lang.Unroll

class MavenSpec extends BeanContextSpec {

    void 'test use defaults from parent pom'() {
        given:
        GeneratorContext generatorContext = buildGeneratorContext([], new Options(null, null, BuildTool.MAVEN))

        when:
        String template = pom.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                generatorContext.getBuildProperties().getProperties(),
                []
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
        Features features = getFeatures([], null, null, BuildTool.MAVEN)
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), features, [], []).render().toString()

        then:
        template.contains('''
          <annotationProcessorPaths combine.children="append">
          </annotationProcessorPaths>
''')

        when:
        features = getFeatures([], Language.KOTLIN, null, BuildTool.MAVEN)
        template = pom.template(ApplicationType.DEFAULT, buildProject(), features, [], []).render().toString()

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
        template = pom.template(ApplicationType.DEFAULT, buildProject(), features, [], []).render().toString()

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
    void 'test micronaut runtime for #feature'(ApplicationType applicationType, String feature, String runtime) {
        given:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11)
        Features features = getFeatures([feature], options, applicationType)
        println features.features

        when:
        String template = pom.template(applicationType, buildProject(), features, []).render().toString()

        then:
        template.contains("<micronaut.runtime>${runtime}</micronaut.runtime>")

        where:
        applicationType             | feature                       | runtime
        ApplicationType.DEFAULT     | "google-cloud-function"       | "google_function"
        ApplicationType.DEFAULT     | "oracle-function"             | "oracle_function"
        ApplicationType.DEFAULT     | "azure-function"              | "azure_function"
        ApplicationType.DEFAULT     | "aws-lambda"                  | "lambda"
        ApplicationType.DEFAULT     | "tomcat-server"               | "tomcat"
        ApplicationType.DEFAULT     | "jetty-server"                | "jetty"
        ApplicationType.DEFAULT     | "netty-server"                | "netty"
        ApplicationType.DEFAULT     | "undertow-server"             | "undertow"
    }

}
