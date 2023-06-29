package io.micronaut.starter.feature.testresources

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class TestResourcesSpec extends ApplicationContextSpec {

    void 'test maven testresources generates dependency when language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features([TestResources.NAME])
                .language(language)
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.testresources</groupId>
      <artifactId>micronaut-test-resources-client</artifactId>
      <scope>provided</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }
}
