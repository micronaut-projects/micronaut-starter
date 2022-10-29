package io.micronaut.starter.feature.function.azure

import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.*
import spock.lang.Unroll

class AzureCloudFunctionSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll("#jdkVersion not supported for #feature")
    void "verify for not supported JDK Versions by azure-function an IllegalArgumentException is thrown"(ApplicationType applicationType, JdkVersion jdkVersion, String feature) {
        when:
        generate(
                applicationType,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                [feature],
        )
        then:
        thrown(IllegalArgumentException)

        where:
        [applicationType, jdkVersion, feature] << [
                [ApplicationType.FUNCTION, ApplicationType.DEFAULT],
                ((JdkVersion.values() as List<JdkVersion>) - AzureFeatureValidatorSpec.supportedVersionsByAzureFunction()) as List<JdkVersion>,
                ['azure-function']
        ].combinations()
    }

    @Unroll("#jdkVersion supported for #feature")
    void "verify for supported JDK Versions by azure-function no exception is thrown"(ApplicationType applicationType, JdkVersion jdkVersion, String feature) {
        when:
        generate(
                applicationType,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, jdkVersion),
                [feature],
        )
        then:
        noExceptionThrown()

        where:
        [applicationType, jdkVersion, feature] << [
                [ApplicationType.FUNCTION, ApplicationType.DEFAULT],
                AzureFeatureValidatorSpec.supportedVersionsByAzureFunction(),
                ['azure-function']
        ].combinations()
    }

    @Unroll
    void 'test gradle raw azure function feature for language=#language'() {
        when:
        def output = generate(
                ApplicationType.FUNCTION,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8),
                ['azure-function']
        )
        String build = output['build.gradle']
        def readme = output["README.md"]

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

    @Unroll
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

    @Unroll
    void 'test sources generated for azure function feature gradle and language=#language (using serde #useSerde)'(Language language, boolean useSerde) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.GRADLE, JdkVersion.JDK_8),
                ['azure-function'] + (useSerde ? ['serialization-jackson'] : [])
        )
        def readme = output["README.md"]

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
                .contains("HttpRequestMessageBuilder")

        where:
        [language, useSerde] << [Language.values().toList(), [true, false]].combinations()
    }

    @Unroll
    void 'test azure function feature for language=#language (using serde #useSerde) - maven'(Language language, boolean useSerde) {
        when:
        def output = generate(
                ApplicationType.DEFAULT,
                new Options(language, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_8),
                ['azure-function'] + (useSerde ? ['serialization-jackson'] : [])
        )
        String build = output['pom.xml']
        def readme = output["README.md"]

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
              .contains("HttpRequestMessageBuilder")

        where:
        [language, useSerde] << [Language.values().toList(), [true, false]].combinations()
    }
}
