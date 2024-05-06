package io.micronaut.starter.feature.build.maven

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import spock.lang.Shared
import spock.lang.Unroll

class MavenSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    Maven maven = beanContext.getBean(Maven)

    void "maven is a BuildFeature"() {
        expect:
        !maven.isGradle()
        maven.isMaven()
    }

    void 'Readme has Maven plugin docs (lang = #lang, apptype = #apptype)'(ApplicationType apptype, Language lang) {
        when:
        Map<String, String> output = generate(apptype, createOptions(lang, BuildTool.MAVEN))
        String readme = output["README.md"]

        then:
        readme
        readme.contains(Maven.MICRONAUT_MAVEN_DOCS_URL)

        where:
        [lang, apptype] << [ Language.values().toList(), ApplicationType.values()].combinations()
    }

    void "multi-module-pom isn't created for single-module builds"() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.DEFAULT_OPTION, BuildTool.MAVEN)
        GeneratorContext generatorContext = buildGeneratorContext([], options, ApplicationType.DEFAULT)

        then:
        generatorContext.templates.mavenPom
        !generatorContext.templates.'multi-module-pom'
    }

    void "multi-module-pom is created for multi-module builds"() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.DEFAULT_OPTION, BuildTool.MAVEN)
        GeneratorContext generatorContext = buildGeneratorContext(['aws-cdk'], options, ApplicationType.DEFAULT)

        then:
        generatorContext.templates.mavenPom
        generatorContext.templates.'multi-module-pom'
    }

    void 'enforcer plugin is added to pom.xml'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .render()
        then:
        template.contains('''\
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
      </plugin>
''')
    }

    void 'test use defaults from parent pom'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .render()
        then: 'parent pom is used'
        template.contains("""
  <parent>
    <groupId>io.micronaut.platform</groupId>
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
        and: 'it contains children-specific properties'
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
                 <groupId>io.micronaut.validation</groupId>
                 <artifactId>micronaut-validation-processor</artifactId>
                 <version>${micronaut.validation.version}</version>
               </annotationProcessorPath>
              </annotationProcessorPaths>
''')

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .render()

        then:
        template.contains('''\
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-inject-groovy</artifactId>
      <scope>provided</scope>
    </dependency>
''')
        and: 'validation is not added by default'
        !template.contains('''\
    <dependency>
      <groupId>io.micronaut.validation</groupId>
      <artifactId>micronaut-validation</artifactId>
      <scope>compile</scope>
    </dependency>
''')
        template.contains('''\
      <groupId>org.apache.groovy</groupId>
      <artifactId>groovy</artifactId>
''')
        template.contains("""\
    <groovyVersion>${VersionInfo.getBomVersion("groovy")}</groovyVersion>
""")
    }

    @Unroll
    void 'test micronaut runtime for #chosenFeatures'(ApplicationType applicationType,
                                                      List<String> chosenFeatures,
                                                      String runtime) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(chosenFeatures)
                .language(Language.JAVA)
                .jdkVersion(javaVersionForRuntime(runtime))
                .render()
        then:
        template.contains("<micronaut.runtime>${runtime}</micronaut.runtime>")

        where:
        applicationType             | chosenFeatures                  | runtime
        ApplicationType.DEFAULT     | ["google-cloud-function"]       | "google_function"
        ApplicationType.DEFAULT     | ["oracle-function"]             | "oracle_function"
        ApplicationType.DEFAULT     | ["azure-function"]              | "azure_function"
        ApplicationType.DEFAULT     | ["aws-lambda"]                  | "lambda"
        ApplicationType.DEFAULT     | ["aws-lambda", 'graalvm']       | "lambda"
        ApplicationType.DEFAULT     | ["tomcat-server"]               | "tomcat"
        ApplicationType.DEFAULT     | ["jetty-server"]                | "jetty"
        ApplicationType.DEFAULT     | ["netty-server"]                | "netty"
        ApplicationType.DEFAULT     | ["undertow-server"]             | "undertow"
    }

    private static JdkVersion javaVersionForRuntime(String runtime) {
        // Azure functions support 21 as a preview for functions version 4.x in Linux. Java 21 is not supported in Windows yet
        // https://learn.microsoft.com/en-us/azure/azure-functions/functions-reference-java?tabs=bash%2Cconsumption#supported-versions
        // Google Cloud Function execution environment supports 21 in preview only
        // https://cloud.google.com/functions/docs/concepts/execution-environment#runtimes
        return runtime in ["azure_function",
                           "google_function"
        ] ? JdkVersion.JDK_17 :
                MicronautJdkVersionConfiguration.DEFAULT_OPTION
    }

    private static Options createOptions(Language language, BuildTool buildTool = BuildTool.DEFAULT_OPTION) {
        new Options(language, language.getDefaults().getTest(), buildTool, AwsLambdaFeatureValidator.firstSupportedJdk())
    }

    void 'Selected jdk = #jdk is specified in Maven for lang = #lang'(
            Language lang, JdkVersion jdk
    ) {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(lang, TestFramework.DEFAULT_OPTION, BuildTool.MAVEN, jdk))
        def buildFile =  output["pom.xml"]

        then:
        buildFile
        if (lang == Language.KOTLIN) {
            // has kapt so has to be 17
            assert buildFile.contains("<jdk.version>17</jdk.version>")
        } else {
            assert buildFile.contains("<jdk.version>$jdk.majorVersion</jdk.version>")
        }

        where:
        [lang, jdk] << [
                Language.values(),
                [JdkVersion.JDK_17, JdkVersion.JDK_21]
        ].combinations()
    }
}
