package io.micronaut.starter.command

import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.extension.AbstractGlobalExtension
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.SpecInfo

class CommandSpecListener extends AbstractGlobalExtension {

    @Override
    void visitSpec(SpecInfo specInfo) {
       /* specInfo.addSetupInterceptor(interceptor -> {
            if (interceptor.instance instanceof CommandSpec) {
                interceptor.spec.addListener(new AbstractRunListener() {
                    @Override
                    void error(ErrorInfo error) {
                        System.err.println("Output from command (last 20 lines):")
                        System.err.println("--------------------------------------------------")
                        System.err.println(((CommandSpec) interceptor.instance).baos.toString().readLines().takeRight(20))
                    }
                })
            }
        })*/
    }
}
