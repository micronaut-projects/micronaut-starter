package io.micronaut.starter.cli.config

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup

class UpdateCliConfigCommandSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run()

    void "test old cli config conversion - #buildTool"() {
        generateProject(Language.JAVA, buildTool)
        File cliYaml = new File(dir, "micronaut-cli.yml").getCanonicalFile()
        //Replace the config with the old style
        cliYaml.write("""profile: service
defaultPackage: micronaut.testing.keycloak
---
testFramework: junit
sourceLanguage: java""")
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)

        when:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, consoleOutput)

        then:
        2 * consoleOutput.warning(_)
        codeGenConfig.buildTool == buildTool
        codeGenConfig.sourceLanguage == Language.JAVA
        codeGenConfig.testFramework == TestFramework.JUNIT
        codeGenConfig.applicationType == ApplicationType.DEFAULT
        codeGenConfig.defaultPackage == "micronaut.testing.keycloak"
        codeGenConfig.legacy

        when:
        Integer exitCode = new UpdateCliConfigCommand(codeGenConfig, () -> new FileSystemOutputHandler(dir, consoleOutput), consoleOutput).call()

        then:
        noExceptionThrown()
        exitCode == 0
        cliYaml.text.matches("""applicationType: default
defaultPackage: micronaut.testing.keycloak
testFramework: junit
sourceLanguage: java
buildTool: $buildTool.name
features: \\[.*?\\]

""")
        2 * consoleOutput.out(_)
        1 * consoleOutput.out("For a list of available features, run `mn create-app --list-features`")

        where:
        buildTool << BuildTool.values()
    }
}
