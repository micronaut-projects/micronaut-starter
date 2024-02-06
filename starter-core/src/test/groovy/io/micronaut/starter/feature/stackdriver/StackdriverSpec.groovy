package io.micronaut.starter.feature.stackdriver

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class StackdriverSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature gcp-cloud-trace contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['gcp-cloud-trace'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains('https://cloud.google.com/trace')
        readme.contains("https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#tracing")
    }

}
