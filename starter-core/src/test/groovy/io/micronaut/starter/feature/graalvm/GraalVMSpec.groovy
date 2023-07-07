package io.micronaut.starter.feature.graalvm

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GraalVMSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    GraalVM graalNativeImage = beanContext.getBean(GraalVM)

    @Unroll("feature graalvm works for application type: #applicationType")
    void "feature graalvm works for every type of application type"(ApplicationType applicationType) {
        expect:
        graalNativeImage.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'graalvm feature not supported for groovy and gradle'() {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['graalvm'])
                .language(Language.GROOVY)
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message == 'GraalVM is not supported in Groovy applications'
    }

    void "test maven graalvm feature doesn't add dependencies and processor defined in parent pom"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["graalvm"])
                .render()

        then:
        !template.contains("""
    <dependency>
      <groupId>org.graalvm.nativeimage</groupId>
      <artifactId>svm</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        !template.contains("""
    <dependency>
      <groupId>org.graalvm.sdk</groupId>
      <artifactId>graal-sdk</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        !template.contains("""
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-graal</artifactId>
              <version>\${micronaut.core.version}</version>
            </path>
""")
        template.contains("""
          <compilerArgs>
            <arg>-Amicronaut.processing.group=example.micronaut</arg>
            <arg>-Amicronaut.processing.module=foo</arg>
          </compilerArgs>
""")

        when:
        template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.KOTLIN)
                .features(["graalvm"])
                .render()

        then:
        !template.contains("""
    <dependency>
      <groupId>org.graalvm.nativeimage</groupId>
      <artifactId>svm</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        template.contains('''\
               <annotationProcessorPath>
                 <groupId>io.micronaut</groupId>
                 <artifactId>micronaut-graal</artifactId>
                 <version>${micronaut.core.version}</version>
               </annotationProcessorPath>
''')
        template.contains("""
              <annotationProcessorArgs>
                <annotationProcessorArg>micronaut.processing.group=example.micronaut</annotationProcessorArg>
                <annotationProcessorArg>micronaut.processing.module=foo</annotationProcessorArg>
              </annotationProcessorArgs>
""")
    }

    void 'graalvm feature not supported for Groovy and maven'() {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.GROOVY)
                .features(["graalvm"])
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message == 'GraalVM is not supported in Groovy applications'
    }

    @Unroll
    void 'Application file is generated for a default application type with gradle and features graalvm & aws-lambda for language: #language'(Language language, String extension) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                ['graalvm', 'aws-lambda']
        )

        then:
        output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())

        where:
        language << GraalVMFeatureValidator.supportedLanguages()
        extension << GraalVMFeatureValidator.supportedLanguages()*.extension
    }

}
