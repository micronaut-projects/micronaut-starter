package io.micronaut.starter.cli.test

import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.command.project.bean.CreateBeanCommand
import io.micronaut.starter.feature.crac.Crac
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.BuildToolCombinations
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class CreateBeanSpec extends CommandSpec {

    @Unroll
    void "test creating a bean - java and #build.getName()"(BuildTool build) {
        generateProject(Language.JAVA, build)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateBeanCommand command = new CreateBeanCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.beanName = "Greeting"

        when:
        Integer exitCode = command.call()
        File bean = new File(dir, "src/main/java/example/micronaut/Greeting.java")

        then:
        exitCode == 0
        bean.exists()
        1 * consoleOutput.out({ it.contains("Rendered bean") })

        when:
        new File(dir, "src/main/java/example/micronaut/Application.java").write("""
package example.micronaut;

import io.micronaut.runtime.Micronaut;
import io.micronaut.context.ApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = Micronaut.run(Application.class);
        ctx.getBean(example.micronaut.Greeting.class);
    }
}
""")
        String output = null
        if (build.isGradle()) {
            output = executeGradle("test")?.output
        } else if (build == BuildTool.MAVEN) {
            output = executeMaven("compile test")
        }

        then:
        output?.contains("BUILD SUCCESS")

        where:
        build << BuildToolCombinations.buildTools
    }

    @Unroll
    void "test creating a bean - groovy and #build.getName()"(BuildTool build) {
        generateProject(Language.GROOVY, build)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateBeanCommand command = new CreateBeanCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.beanName = "Greeting"

        when:
        Integer exitCode = command.call()
        File bean = new File(dir, "src/main/groovy/example/micronaut/Greeting.groovy")

        then:
        exitCode == 0
        bean.exists()
        1 * consoleOutput.out({ it.contains("Rendered bean") })

        when:
        new File(dir, "src/main/groovy/example/micronaut/Application.groovy").write("""
package example.micronaut

import io.micronaut.runtime.Micronaut
import io.micronaut.context.ApplicationContext

class Application {

    static void main(String[] args) {
        ApplicationContext ctx = Micronaut.run(Application)
        ctx.getBean(example.micronaut.Greeting)
    }
}
""")
        String output = null
        if (build.isGradle()) {
            output = executeGradle("test")?.output
        } else if (build == BuildTool.MAVEN) {
            output = executeMaven("compile test")
        }

        then:
        output?.contains("BUILD SUCCESS")

        where:
        build << BuildToolCombinations.buildTools
    }

    @Unroll
    void "test creating a bean - kotlin and #build.getName()"(BuildTool build) {
        generateProject(Language.KOTLIN, build)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateBeanCommand command = new CreateBeanCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.beanName = "Greeting"

        when:
        Integer exitCode = command.call()
        File bean = new File(dir, "src/main/kotlin/example/micronaut/Greeting.kt")

        then:
        exitCode == 0
        bean.exists()
        1 * consoleOutput.out({ it.contains("Rendered bean") })

        when:
        new File(dir, "src/main/kotlin/example/micronaut/Application.kt").write("""
package example.micronaut

import io.micronaut.runtime.Micronaut

fun main(args: Array<String>) {
    val ctx = Micronaut.build()
            .packages("example.micronaut")
            .start()
    ctx.getBean(example.micronaut.Greeting::class.java)
}
""")
        String output = null
        if (build.isGradle()) {
            output = executeGradle("test")?.output
        } else if (build == BuildTool.MAVEN) {
            output = executeMaven("compile test")
        }

        then:
        output?.contains("BUILD SUCCESS")

        where:
        build << BuildToolCombinations.buildTools
    }

    @Override
    String getTempDirectoryPrefix() {
        "test-createBeans"
    }
}
