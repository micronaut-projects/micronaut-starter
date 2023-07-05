package io.micronaut.starter.feature.microstream

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

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
                .features(['microstream', 'kapt'])
                .render()

        then:
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream")')
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream-annotations")')
        if (language == Language.KOTLIN) {
            assert template.contains('kapt("io.micronaut.microstream:micronaut-microstream-annotations")')
        } else if (language == Language.JAVA) {
            assert template.contains('annotationProcessor("io.micronaut.microstream:micronaut-microstream-annotations")')
        } else {
            assert !template.contains('kapt("io.micronaut.microstream:micronaut-microstream-annotations")')
            assert !template.contains('annotationProcessor("io.micronaut.microstream:micronaut-microstream-annotations")')
        }

        where:
        language << Language.values().toList()
    }

    void 'test gradle microstream-rest feature for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .features(['microstream-rest', 'kapt'])
                .render()

        then:
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream")')
        template.contains('implementation("io.micronaut.microstream:micronaut-microstream-annotations")')
        template.contains('developmentOnly("io.micronaut.microstream:micronaut-microstream-rest")')
        if (language == Language.KOTLIN) {
            assert template.contains('kapt("io.micronaut.microstream:micronaut-microstream-annotations")')
        } else if (language == Language.JAVA) {
            assert template.contains('annotationProcessor("io.micronaut.microstream:micronaut-microstream-annotations")')
        } else {
            assert !template.contains('kapt("io.micronaut.microstream:micronaut-microstream-annotations")')
            assert !template.contains('annotationProcessor("io.micronaut.microstream:micronaut-microstream-annotations")')
        }

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
        (language != Language.GROOVY) == template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        (language == Language.GROOVY) == template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-annotations</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        if (language == Language.KOTLIN) {
            assert template.contains('''
               <annotationProcessorPath>
                 <groupId>io.micronaut.microstream</groupId>
                 <artifactId>micronaut-microstream-annotations</artifactId>
                 <version>${micronaut.microstream.version}</version>
               </annotationProcessorPath>''')
        } else if(language == Language.JAVA) {
            assert template.contains('''
            <path>
              <groupId>io.micronaut.microstream</groupId>
              <artifactId>micronaut-microstream-annotations</artifactId>
              <version>${micronaut.microstream.version}</version>
            </path>''')
        }

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
        (language != Language.GROOVY) == template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-annotations</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        (language == Language.GROOVY) == template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-annotations</artifactId>
      <scope>provided</scope>
    </dependency>
""")
        template.contains("""
    <dependency>
      <groupId>io.micronaut.microstream</groupId>
      <artifactId>micronaut-microstream-rest</artifactId>
      <scope>provided</scope>
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

        where:
        language << Language.values().toList()
    }

    void 'test microstream configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream'])

        then: "we don't configure storage as there are no sensible defaults"
        !commandContext.configuration.keySet().any { it.startsWith('microstream.storage.') }
    }

    void 'test microstream-rest configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream-rest'])

        then: "no configuration is created"
        !commandContext.configuration.keySet().any { it.startsWith('microstream.storage.') }
        !commandContext.configuration.keySet().any { it.startsWith('microstream.rest.') }
    }

    void 'test microstream-cache configuration'() {
        when:
        GeneratorContext commandContext = buildGeneratorContext(['microstream-cache'])

        then:
        !commandContext.configuration.keySet().any { it.startsWith('microstream.storage.') }
        !commandContext.configuration.keySet().any { it.startsWith('microstream.rest.') }
        commandContext.configuration.'microstream.cache.my-cache.key-type' == 'java.lang.Integer'
        commandContext.configuration.'microstream.cache.my-cache.value-type' == 'java.lang.String'
    }

    @Unroll
    void 'test feature is not preview for #microStreamFeature.name'(MicroStreamFeature microStreamFeature) {
        expect:
        !microStreamFeature.isPreview()

        where:
        microStreamFeature << beanContext.getBeansOfType(MicroStreamFeature).iterator()
    }
}
