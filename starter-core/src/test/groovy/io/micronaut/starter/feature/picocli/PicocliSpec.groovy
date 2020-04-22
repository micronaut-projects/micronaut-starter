package io.micronaut.starter.feature.picocli

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.feature.Features
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class PicocliSpec extends BeanContextSpec {

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
        features.contains("picocli-kotlintest")
        features.contains("kotlintest")
        generatorContext.getTemplates().containsKey("picocliKotlinTest")
        !features.contains("picocli-junit")
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
