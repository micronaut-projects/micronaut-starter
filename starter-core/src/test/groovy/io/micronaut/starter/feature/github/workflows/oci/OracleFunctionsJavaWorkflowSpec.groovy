package io.micronaut.starter.feature.github.workflows.oci

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.function.CloudProvider
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class OracleFunctionsJavaWorkflowSpec extends BeanContextSpec {
    @Shared
    @Subject
    OracleFunctionsJavaWorkflow oracleFunctionsJavaWorkflow = beanContext.getBean(OracleFunctionsJavaWorkflow)

    @Unroll
    void "OracleFunctionsJavaWorkflow belongs to cloud ORACLE"() {
        expect:
        oracleFunctionsJavaWorkflow.cloudProvider.isPresent()
        CloudProvider.ORACLE == oracleFunctionsJavaWorkflow.cloudProvider.get()
    }
}
