package io.micronaut.starter.feature.function.azure

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class AzureCloudFunctionSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void "verify for #jdkVersion application #applicationType with azure-function no exception is thrown"(ApplicationType applicationType, JdkVersion jdkVersion) {
        when:
        generate(
                applicationType,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                ['azure-function']
        )
        then:
        noExceptionThrown()

        where:
        [applicationType, jdkVersion] << [
                [ApplicationType.FUNCTION, ApplicationType.DEFAULT],
                MicronautJdkVersionConfiguration.SUPPORTED_JDKS.findAll { AzureFunctionFeatureValidator.supports(it) }
        ].combinations()
    }

    void "verify for #jdkVersion application #applicationType with azure-function illegal argument exception is thrown"(ApplicationType applicationType, JdkVersion jdkVersion) {
        when:
        generate(
                applicationType,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                ['azure-function']
        )
        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == 'Azure Function currently only supports JDK 8, 11 and 17 -- https://learn.microsoft.com/en-us/azure/developer/java/fundamentals/java-support-on-azure'

        where:
        [applicationType, jdkVersion] << [
                [ApplicationType.FUNCTION, ApplicationType.DEFAULT],
                JdkVersion.JDKS.findAll { !AzureFunctionFeatureValidator.supports(it) }
        ].combinations()
    }

    void 'test gradle raw azure function feature for language=#language'() {
        when:
        Map<String, String> output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8),
                ['azure-function']
        )
        String build = output['build.gradle']
        String readme = output["README.md"]

        then:
        build.contains('id("com.microsoft.azure.azurefunctions")')
        !build.contains('implementation("io.micronaut:micronaut-function-aws")')
        !build.contains('implementation("io.micronaut.azure:micronaut-azure-function-http")')
        build.contains('implementation("io.micronaut.azure:micronaut-azure-function")')
        build.contains('implementation("com.microsoft.azure.functions:azure-functions-java-library")')
        build.contains('azurefunctions {')
        build.contains('os = "linux"')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')
        !build.contains('"com.github.johnrengelman.shadow"')
        !build.contains('shadowJar')
        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        !output.containsKey("$srcDir/example/micronaut/Book.$extension".toString())
        !output.containsKey("$srcDir/example/micronaut/FooController.$extension".toString())
        output.containsKey("$srcDir/example/micronaut/Function.$extension".toString())
        output.get("$srcDir/example/micronaut/Function.$extension".toString())
                .contains(" AzureFunction")
        output.containsKey(Language.JAVA.testSrcDir + "/example/micronaut/HttpRequest.$Language.JAVA.extension".toString())
        output.containsKey(Language.JAVA.testSrcDir + "/example/micronaut/ResponseBuilder.$Language.JAVA.extension".toString())

        output.containsKey("$testSrcDir/example/micronaut/FunctionTest.$extension".toString())
        output.get("$testSrcDir/example/micronaut/FunctionTest.$extension".toString())
                .contains("function.hello")

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    void 'test gradle azure function feature for language=#language'() {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .applicationType(ApplicationType.DEFAULT)
                .testFramework(TestFramework.JUNIT)
                .jdkVersion(JdkVersion.JDK_8)
                .features(['azure-function'])
                .render()
        then:
        build.contains('runtime("azure_function")')
        build.contains('azurefunctions {')
        build.contains('os = "linux"')
        !build.contains('implementation "io.micronaut:micronaut-http-server-netty"')
        !build.contains('implementation "io.micronaut:micronaut-http-client"')
        !build.contains('"com.github.johnrengelman.shadow"')
        !build.contains('shadowJar')

        when:
        String pluginId = 'com.microsoft.azure.azurefunctions'
        String applyPlugin = 'id("' + pluginId + '") version "'

        then:
        build.contains(applyPlugin)

        when:
        Optional<SemanticVersion> semanticVersionOptional = parseCommunityGradlePluginVersion(pluginId, build).map(SemanticVersion::new)

        then:
        noExceptionThrown()
        semanticVersionOptional.isPresent()

        where:
        language << Language.values().toList()
        extension << Language.extensions()
        srcDir << Language.srcDirs()
        testSrcDir << Language.testSrcDirs()
    }

    void 'test sources generated for azure function feature gradle and language=#language (using serde #useSerde)'(Language language, boolean useSerde) {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8),
                ['azure-function'] + (useSerde ? ['serialization-jackson'] : ['jackson-databind'])
        )
        String readme = output["README.md"]

        then:
        output.containsKey("${language.srcDir}/example/micronaut/Application.${language.extension}".toString())

        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        output.containsKey("$language.srcDir/example/micronaut/FooController.$language.extension".toString())
        useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Serdeable")
        !useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Introspected")

        output.get("$language.srcDir/example/micronaut/Function.$language.extension".toString())
                .contains(" AzureHttpFunction")
        output.containsKey("$language.srcDir/example/micronaut/Function.$language.extension".toString())
        output.containsKey("$language.testSrcDir/example/micronaut/FooFunctionTest.$language.extension".toString())
        output.get("$language.testSrcDir/example/micronaut/FooFunctionTest.$language.extension".toString())
                .contains("response = function.request")

        where:
        [language, useSerde] << [Language.values().toList(), [true, false]].combinations()
    }

    void 'test azure function feature for language=#language (using serde #useSerde) - maven'(Language language, boolean useSerde) {
        when:
        Map<String, String> output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_8),
                ['azure-function'] + (useSerde ? ['serialization-jackson'] : ['jackson-databind'])
        )
        String build = output['pom.xml']
        String readme = output["README.md"]

        then:
        build.count('<artifactId>azure-functions-maven-plugin</artifactId>') == 1
        build.contains('<artifactId>azure-functions-java-library</artifactId>')
        !build.contains('<artifactId>micronaut-http-server-netty</artifactId>')
        !build.contains("<artifactId>maven-shade-plugin</artifactId>")
        output.containsKey("${language.srcDir}/example/micronaut/Application.${language.extension}".toString())

        readme?.contains("Micronaut and Azure Function")
        output.containsKey("host.json")
        output.containsKey("local.settings.json")
        output.containsKey("$language.srcDir/example/micronaut/FooController.$language.extension".toString())
        useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Serdeable")
        !useSerde == output."${language.srcDir}/example/micronaut/FooController.${language.extension}".contains("@Introspected")

        output.containsKey("$language.srcDir/example/micronaut/Function.$language.extension".toString())
        output.containsKey("$language.testSrcDir/example/micronaut/FooFunctionTest.$language.extension".toString())
        output.get("$language.testSrcDir/example/micronaut/FooFunctionTest.$language.extension".toString())
              .contains("response = function.request")

        where:
        [language, useSerde] << [Language.values().toList(), [true, false]].combinations()
    }
}
