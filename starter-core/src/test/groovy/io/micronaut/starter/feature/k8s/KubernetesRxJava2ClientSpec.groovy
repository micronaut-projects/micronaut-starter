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

class KubernetesRxJava2ClientSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature kubernetes-rxjava2-client contains links to docs'() {
        when:
        Map<String, String> output = generate(['kubernetes-rxjava2-client'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/kubernetes-client/java/wiki")
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-client")
    }

    @Subject
    @Shared
    KubernetesRxJava2Client client = beanContext.getBean(KubernetesRxJava2Client)

    void "kubernetes-rxjava2-client belongs to Client category"() {
        expect:
        Category.CLIENT == client.category
    }

    void "kubernetes-rxjava2-client title and description are different"() {
        expect:
        client.getTitle()
        client.getDescription()
        client.getTitle() != client.getDescription()
    }

    @Unroll("feature kubernetes-rxjava2-client works for application type: #applicationType")
    void "feature kubernetes-rxjava2-client works for every type of application type"(ApplicationType applicationType) {
        expect:
        client.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature kubernetes-rxjava2-client for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['kubernetes-rxjava2-client'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kubernetes</groupId>
      <artifactId>micronaut-kubernetes-client-rxjava2</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        then:
        template.contains("""
    <dependency>
      <groupId>io.reactivex.rxjava2</groupId>
      <artifactId>rxjava</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and kubernetes-rxjava2-client reactor for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['kubernetes-rxjava2-client'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-client-rxjava2")')
        template.contains('implementation("io.reactivex.rxjava2:rxjava")')

        where:
        language << Language.values()
    }

    void 'rxjava2 core dependency is not present if rxjava2 feature is present'(){
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kubernetes-rxjava2-client', 'rxjava2'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-client-rxjava2")')
        !template.contains('implementation("io.reactivex.rxjava2:rxjava")')
    }
}
