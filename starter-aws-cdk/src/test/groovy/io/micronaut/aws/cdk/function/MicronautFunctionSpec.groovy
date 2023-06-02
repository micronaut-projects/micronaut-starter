package io.micronaut.aws.cdk.function

import io.micronaut.function.aws.proxy.MicronautLambdaHandler
import io.micronaut.starter.application.ApplicationType
import software.amazon.awscdk.assertions.Template
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.lambda.Code
import spock.lang.Shared
import spock.lang.Specification
import software.amazon.awscdk.services.lambda.Runtime
import software.amazon.awscdk.Stack
import spock.lang.Unroll;

class MicronautFunctionSpec extends Specification {

    @Unroll
    void "MicronautFunction::create sets Handler and #runtime for #applicationType"(ApplicationType applicationType, boolean graalVMNative, Runtime runtime) {
        given:
        Stack stack = new Stack()

        when:
        Function.Builder builder = MicronautFunction.create(applicationType, graalVMNative, stack, "myfunction")
                .code(Code.fromAsset("src"))
        if (handler != null) {
            builder = builder.handler(handler)
        }
        Function function = builder.build()
        Template template = Template.fromStack(stack)

        then:
        noExceptionThrown()
        runtime == function.runtime
        if (applicationType == ApplicationType.DEFAULT) {
            template.hasResourceProperties("AWS::Lambda::Function", ["Handler": handler != null ? handler : MicronautLambdaHandler.class.getName()])
        }

        where:
        applicationType          | graalVMNative | runtime                | handler
        ApplicationType.DEFAULT  | true          | Runtime.PROVIDED_AL2   | null
        ApplicationType.DEFAULT  | false         | Runtime.JAVA_11        | null
        ApplicationType.FUNCTION | true          | Runtime.PROVIDED_AL2   | "example.micronaut.MyHandler"
        ApplicationType.FUNCTION | false         | Runtime.JAVA_11        | "example.micronaut.MyHandler"
    }
}
