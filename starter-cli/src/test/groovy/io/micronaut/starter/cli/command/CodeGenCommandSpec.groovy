package io.micronaut.starter.cli.command

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.cli.CodeGenConfig
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class CodeGenCommandSpec extends Specification {

    @Shared @AutoCleanup ApplicationContext beanContext = ApplicationContext.run()

    void "test all codegen commands can be created"() {
        CodeGenConfig codeGenConfig = new CodeGenConfig()

        when:
        beanContext.getBeanDefinitions(CodeGenCommand.class).stream()
                .map(bd -> bd.getBeanType())
                .forEach(bt -> beanContext.createBean(bt, codeGenConfig))

        then:
        noExceptionThrown()
    }
}
