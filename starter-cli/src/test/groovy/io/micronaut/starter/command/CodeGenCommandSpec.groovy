package io.micronaut.starter.command

import io.micronaut.context.BeanContext
import io.micronaut.inject.BeanDefinition
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.command.CodeGenCommand
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class CodeGenCommandSpec extends Specification {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

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
