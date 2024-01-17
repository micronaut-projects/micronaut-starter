package io.micronaut.starter.feature.messaging.jms

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class SQSSpec  extends ApplicationContextSpec {

    void 'test localstack-sqs test-resources module is added'() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jms-sqs', 'test-resources'])
                .language(Language.JAVA)
                .render()

        then:
        template.contains("""
    testResources {
        additionalModules.add("localstack-sqs")
    }""")

        where:
        language << Language.values().toList()
    }
}