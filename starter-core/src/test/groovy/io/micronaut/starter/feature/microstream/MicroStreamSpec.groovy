package io.micronaut.starter.feature.microstream

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

class MicroStreamSpec extends BeanContextSpec implements CommandOutputFixture {

    void 'test readme.md with feature microstream contains links to micronaut docs'() {
        when:
        def output = generate(['microstream'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut MicroStream documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide)")
        readme.contains("[https://microstream.one/](https://microstream.one/)")
    }

    void 'test readme.md with feature microstream-rest contains links to micronaut docs'() {
        when:
        def output = generate(['microstream-rest'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut MicroStream documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide)")
        readme.contains("[https://microstream.one/](https://microstream.one/)")
        readme.contains("[Micronaut MicroStream REST documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide/#rest)")
        readme.contains("[https://docs.microstream.one/manual/storage/rest-interface/index.html](https://docs.microstream.one/manual/storage/rest-interface/index.html)")
    }

    void 'test readme.md with feature microstream-cache contains links to micronaut docs'() {
        when:
        def output = generate(['microstream-cache'])
        def readme = output["README.md"]

        then:
        !readme.contains("[Micronaut MicroStream documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide)")
        !readme.contains("[https://microstream.one/](https://microstream.one/)")
        readme.contains("[Micronaut MicroStream Cache documentation](https://micronaut-projects.github.io/micronaut-microstream/latest/guide/#cache)")
        readme.contains("[https://docs.microstream.one/manual/cache/index.html](https://docs.microstream.one/manual/cache/index.html)")
    }

    void 'test gradle microstream feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['microstream'])
                .render()

        then:
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream")')
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream-annotations")')

        where:
        language << Language.values().toList()
    }

    void 'test gradle microstream-rest feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['microstream-rest'])
                .render()

        then:
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream")')
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream-annotations")')
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream-rest")')

        where:
        language << Language.values().toList()
    }

    void 'test gradle microstream-cache feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['microstream-cache'])
                .render()

        then:
        !template.contains('implementation("io.micronaut.microstream:micronaut-microstream")')
        !template.contains('implementation("io.micronaut.microstream:micronaut-microstream-annotations")')
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream-cache")')
        template.contains('implementation("io.micronaut.cache:micronaut-cache-core")')

        where:
        language << Language.values().toList()
    }

    void 'test maven microstream feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['microstream'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    void 'test maven microstream-rest feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['microstream-rest'])
                .render()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-rest</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    void 'test maven microstream-cache feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['microstream-cache'])
                .render()

        then:
        !template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream</artifactId>
      <scope>compile</scope>
    </dependency>
    """)
        !template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-cache</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.cache</groupId>
      <artifactId>micronaut-cache-core</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << Language.values().toList()
    }

    void 'test microstream configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream'])

        then:
        commandContext.configuration.'microstream.storage.main.storage-directory' == '/tmp/microstream'
        commandContext.configuration.'microstream.storage.main.root-class' == 'java.util.ArrayList'
    }

    void 'test microstream-rest configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream-rest'])

        then:
        commandContext.configuration.'microstream.storage.main.storage-directory' == '/tmp/microstream'
        commandContext.configuration.'microstream.storage.main.root-class' == 'java.util.ArrayList'
        commandContext.configuration.'microstream.rest.enabled' == 'true'
        commandContext.configuration.'microstream.rest.path' == 'microstream'
    }

    void 'test microstream-cache configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream-cache'])

        then:
        !commandContext.configuration.'microstream.storage.main.storage-directory'
        !commandContext.configuration.'microstream.storage.main.root-class'
        !commandContext.configuration.'microstream.rest.enabled'
        !commandContext.configuration.'microstream.rest.path'
        commandContext.configuration.'microstream.storage.cachestore.storage-directory' == '/tmp/cache'
        commandContext.configuration.'microstream.cache.my-cache.key-type' == 'java.lang.Integer'
        commandContext.configuration.'microstream.cache.my-cache.value-type' == 'java.lang.String'
        commandContext.configuration.'microstream.cache.my-cache.backing-storage' == 'cachestore'
    }
}
