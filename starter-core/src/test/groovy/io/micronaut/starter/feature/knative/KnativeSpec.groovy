package io.micronaut.starter.feature.knative

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture

class KnativeSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature knative contains links to micronaut and knative docs'() {
        when:
        Map<String, String> output = generate(['knative'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html")
        readme.contains("https://knative.dev/")
    }

    void 'test knative configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['knative'])

        then:
        commandContext.templates.get('knativeYaml')
    }

}
