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

class KubernetesReactorClientSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature kubernetes-reactor-client contains links to docs'() {
        when:
        Map<String, String> output = generate(['kubernetes-reactor-client'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/kubernetes-client/java/wiki")
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-client")
    }

    @Subject
    @Shared
    KubernetesReactorClient client = beanContext.getBean(KubernetesReactorClient)

    void "kubernetes-reactor-client belongs to Client category"() {
        expect:
        Category.CLIENT == client.category
    }

    void "kubernetes-reactor-client title and description are different"() {
        expect:
        client.getTitle()
        client.getDescription()
        client.getTitle() != client.getDescription()
    }

    @Unroll("feature kubernetes-reactor-client works for application type: #applicationType")
    void "feature kubernetes-reactor-client works for every type of application type"(ApplicationType applicationType) {
        expect:
        client.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature kubernetes-reactor-client for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['kubernetes-reactor-client'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kubernetes</groupId>
      <artifactId>micronaut-kubernetes-client-reactor</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        then:
        template.contains("""
    <dependency>
      <groupId>io.projectreactor</groupId>
      <artifactId>reactor-core</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and kubernetes-reactor-client reactor for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['kubernetes-reactor-client'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-client-reactor")')
        template.contains('implementation("io.projectreactor:reactor-core")')

        where:
        language << Language.values()
    }

    void 'reactor core dependency is not present if reactor feature is present'(){
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kubernetes-reactor-client', 'reactor'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-client-reactor")')
        !template.contains('implementation("io.projectreactor:reactor-core")')
    }
}
