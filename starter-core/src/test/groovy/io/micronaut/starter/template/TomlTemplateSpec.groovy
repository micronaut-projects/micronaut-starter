package io.micronaut.starter.template

import io.micronaut.starter.feature.config.Configuration
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class TomlTemplateSpec extends Specification {
    private static String toml(Configuration configuration) {
        def template = new TomlTemplate(null, configuration)
        def bos = new ByteArrayOutputStream()
        template.write(bos)
        return new String(bos.toByteArray(), StandardCharsets.UTF_8)
    }

    def 'simple'() {
        given:
        def config = new Configuration('', '', '')
        config.put('foo.bar', 4)
        config.put('foo.baz', ['foo': 123, 'bar': [1, 2, ['name': 'fox']]])

        expect:
        toml(config) == '''\
foo.bar = 4

[foo.baz]
foo = 123
bar = [1, 2, {name = 'fox'}]
'''
    }

    def 'single nested value uses table'() {
        given:
        def config = new Configuration('', '', '')
        config.put('micronaut.application.name', 'demo')

        expect:
        toml(config) == '''\
[micronaut.application]
name = 'demo'
'''
    }
}
