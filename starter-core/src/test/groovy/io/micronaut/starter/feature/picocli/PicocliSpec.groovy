package io.micronaut.starter.feature.picocli

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class PicocliSpec extends ApplicationContextSpec {

    @Unroll
    void 'test gradle cli app contains picocli-gen as annotation processor for language=#language'(Language language, String scope) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .applicationType(ApplicationType.CLI)
                .render()

        then:
        template.contains("$scope(\"info.picocli:picocli-codegen\")")
        if (language in [Language.KOTLIN, Language.GROOVY]) {
            assert !template.contains('annotationProcessor("info.picocli:picocli-codegen')
        }
        template.contains('implementation("info.picocli:picocli")')
        template.contains('implementation("io.micronaut.picocli:micronaut-picocli")')

        where:
        language        | scope
        Language.JAVA   | "annotationProcessor"
        Language.KOTLIN | "kapt"
        Language.GROOVY | "compileOnly"
    }

    void 'test maven cli app JAVA contains picocli-gen as annotation processor'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.JAVA)
                .applicationType(ApplicationType.CLI)
                .render()

        then:
        template.contains('''\
            <path>
              <groupId>info.picocli</groupId>
              <artifactId>picocli-codegen</artifactId>
              <version>${picocli.version}</version>
            </path>
''')
        template.count('''\
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-inject-java</artifactId>
              <version>${micronaut.version}</version>
            </path>
''') == 0
        template.count('''\
            <path>
              <groupId>io.micronaut</groupId>
              <artifactId>micronaut-validation</artifactId>
              <version>${micronaut.version}</version>
            </path>
''') == 0
        template.count('''\
    <dependency>
      <groupId>info.picocli</groupId>
      <artifactId>picocli</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1
        template.count('''\
    <dependency>
      <groupId>io.micronaut.picocli</groupId>
      <artifactId>micronaut-picocli</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        and: 'property is not defined it is inherited via the bom'
        !parsePropertySemanticVersion(template, "picocli.version").isPresent()
    }

    void 'test maven cli app Kotlin contains picocli-gen as annotation processor'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(Language.KOTLIN)
                .applicationType(ApplicationType.CLI)
                .render()

        then:
        template.count('''\
               <annotationProcessorPath>
                 <groupId>info.picocli</groupId>
                 <artifactId>picocli-codegen</artifactId>
                 <version>${picocli.version}</version>
               </annotationProcessorPath>
''') == 1
        template.count('''\
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-inject-java</artifactId>
                  <version>${micronaut.version}</version>
                </annotationProcessorPath>
''') == 2
        template.count('''\
                <annotationProcessorPath>
                  <groupId>io.micronaut</groupId>
                  <artifactId>micronaut-validation</artifactId>
                  <version>${micronaut.version}</version>
                </annotationProcessorPath>
''') == 2
        template.count('''\
    <dependency>
      <groupId>info.picocli</groupId>
      <artifactId>picocli</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1
        template.count('''\
    <dependency>
      <groupId>io.micronaut.picocli</groupId>
      <artifactId>micronaut-picocli</artifactId>
      <scope>compile</scope>
    </dependency>
''') == 1

        and: 'property is not defined it is inherited via the bom'
        !parsePropertySemanticVersion(template, "picocli.version").isPresent()
    }


    void "test the test features are applied"() {
        when:
        Options options = new Options(Language.JAVA, null, BuildTool.GRADLE)
        Features features = getFeatures([], options, ApplicationType.CLI)
        GeneratorContext generatorContext = buildGeneratorContext([], options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains("picocli-junit")
        features.contains("junit")
        generatorContext.getTemplates().containsKey("picocliJunitTest")
        !features.contains("picocli-spock")
        !features.contains("picocli-kotlintest")
        !generatorContext.getTemplates().containsKey("testDir")

        when:
        options = new Options(Language.GROOVY, null, BuildTool.GRADLE)
        features = getFeatures([], options, ApplicationType.CLI)
        generatorContext = buildGeneratorContext([], options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains("picocli-spock")
        features.contains("spock")
        generatorContext.getTemplates().containsKey("picocliSpock")
        !features.contains("picocli-junit")
        !features.contains("picocli-kotlintest")
        !generatorContext.getTemplates().containsKey("testDir")

        when:
        options = new Options(Language.KOTLIN, null, BuildTool.GRADLE)
        features = getFeatures([], options, ApplicationType.CLI)
        generatorContext = buildGeneratorContext([], options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains("picocli-junit")
        features.contains("junit")
        generatorContext.getTemplates().containsKey("picocliJunitTest")
        !features.contains("picocli-kotlintest")
        !features.contains("picocli-spock")
        !generatorContext.getTemplates().containsKey("testDir")

        when:
        options = new Options(language, TestFramework.JUNIT, BuildTool.GRADLE)
        features = getFeatures([], options, ApplicationType.CLI)
        generatorContext = buildGeneratorContext([], options, ApplicationType.CLI)

        then:
        features.contains("picocli")
        features.contains("picocli-junit")
        features.contains("junit")
        generatorContext.getTemplates().containsKey("picocliJunitTest")
        !features.contains("picocli-spock")
        !features.contains("picocli-kotlintest")
        !generatorContext.getTemplates().containsKey("testDir")

        where:
        language << Language.values()
    }
}
