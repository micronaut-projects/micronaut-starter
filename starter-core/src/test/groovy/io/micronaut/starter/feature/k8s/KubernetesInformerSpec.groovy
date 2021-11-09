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

class KubernetesInformerSpec extends ApplicationContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature kubernetes-informer contains links to docs'() {
        when:
        def output = generate(['kubernetes-informer'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://github.com/kubernetes-client/java/wiki")
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-informer")
    }

    @Subject
    @Shared
    KubernetesInformer informer = beanContext.getBean(KubernetesInformer)

    void "kubernetes-informer belongs to Cloud category"() {
        expect:
        Category.CLOUD == informer.category
    }

    void "kubernetes-informer title and description are different"() {
        expect:
        informer.getTitle()
        informer.getDescription()
        informer.getTitle() != informer.getDescription()
    }

    @Unroll("feature kubernetes-informer works for application type: #applicationType")
    void "feature kubernetes-informer works for every type of application type"(ApplicationType applicationType) {
        expect:
        informer.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(['kubernetes-informer'])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kubernetes</groupId>
      <artifactId>micronaut-kubernetes-informer</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['kubernetes-informer'])
                .render()

        then:
        template.contains('implementation("io.micronaut.kubernetes:micronaut-kubernetes-informer")')

        where:
        language << Language.values()
    }
}
