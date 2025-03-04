package io.micronaut.starter.feature.graalvm

import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.projectgen.core.buildtools.Scope
import io.micronaut.starter.feature.aop.AOP
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class GraalVMSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    @Shared
    GraalVM graalNativeImage = beanContext.getBean(GraalVM)

    void "test dependency added for AOP feature"(BuildTool buildTool) {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .features([GraalVM.FEATURE_NAME_GRAALVM])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        !verifier.hasDependency("org.graalvm.nativeimage", "svm", Scope.COMPILE_ONLY)

        where:
        buildTool << BuildTool.valuesGradle()
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
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(language)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE)
                .javaVersion(MicronautJdkVersionConfiguration.DEFAULT_OPTION)
                .features(['graalvm', 'aws-lambda'])
                .build()
        when:
        def output = generate(options)

        then:
        output.containsKey("${language.srcDir}/example/micronaut/Application.${extension}".toString())

        where:
        language << GraalVMFeatureValidator.supportedLanguages()
        extension << GraalVMFeatureValidator.supportedLanguages()*.extension
    }

}
