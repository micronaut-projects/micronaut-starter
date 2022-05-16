package io.micronaut.starter.feature.github.workflows.oci

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.function.CloudProvider
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class OracleFunctionsGraalWorkflowSpec extends BeanContextSpec {
    @Shared
    @Subject
    OracleFunctionsGraalWorkflow oracleFunctionsGraalWorkflow = beanContext.getBean(OracleFunctionsGraalWorkflow)

    @Unroll
    void "OracleFunctionsGraalWorkflow belongs to cloud ORACLE"() {
        expect:
        oracleFunctionsGraalWorkflow.cloudProvider.isPresent()
        CloudProvider.ORACLE == oracleFunctionsGraalWorkflow.cloudProvider.get()
    }
}
