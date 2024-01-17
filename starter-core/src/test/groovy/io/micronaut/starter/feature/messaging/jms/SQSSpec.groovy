package io.micronaut.starter.feature.messaging.jms

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class SQSSpec  extends ApplicationContextSpec {

    void 'test localstack-sqs test-resources module is added'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['jms-sqs', 'test-resources'])
                .language(language)
                .render()

        then:
        template.contains("""
    testResources {
        additionalModules.add("localstack-sqs")
    }""")

        where:
        language << Language.values().toList()
    }

    void 'test localstack-sqs test-resources module is added for Maven'() {
        given:
        BuildTool buildTool = BuildTool.MAVEN
        when:
        String template = new BuildBuilder(beanContext, buildTool)
            .features(['jms-sqs', 'test-resources'])
            .language(Language.JAVA)
            .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)
        then:
        verifier.hasTestResourceDependency("micronaut-test-resources-localstack-sqs")
    }
}

