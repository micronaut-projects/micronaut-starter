package io.micronaut.starter.feature.k8s

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KubernetesClientSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature kubernetes-client contains links to docs'() {
        when:
        def output = generate(['kubernetes-client'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/kubernetes-client/java/wiki")
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-client")
    }

    @Subject
    @Shared
    KubernetesClient client = beanContext.getBean(KubernetesClient)

    void "kubernetes-client belongs to Client category"() {
        expect:
        Category.CLIENT == client.category
    }

    void "kubernetes-client title and description are different"() {
        expect:
        client.getTitle()
        client.getDescription()
        client.getTitle() != client.getDescription()
    }

    @Unroll("feature kubernetes-client works for application type: #applicationType")
    void "feature kubernetes-client works for every type of application type"(ApplicationType applicationType) {
        expect:
        client.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature kubernetes-client for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['kubernetes-client'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kubernetes</groupId>
      <artifactId>micronaut-kubernetes-client</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and kubernetes-client reactor for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['kubernetes-client'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-client")')

        where:
        language << Language.values()
    }
}
