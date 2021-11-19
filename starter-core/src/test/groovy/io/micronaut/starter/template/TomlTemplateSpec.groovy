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

    def 'simple no tables'() {
        given:
        def config = new Configuration('', '', '')
        config.put('foo.bar', 4)
        config.put('foo.baz', ['foo': 123, 'bar': [1, 2, ['name': 'fox']]])

        expect:
        toml(config) == '''foo.bar = 4
foo.baz.foo = 123
foo.baz.bar = [1, 2, {name = 'fox'}]
'''
    }

    def 'simple table'() {
        given:
        def config = new Configuration('', '', '')
        config.registerTable('foo')
        config.put('foo.bar', 4)
        config.registerTable('foo.baz')
        config.put('foo.baz', ['foo': 123, 'bar': [1, 2, ['name': 'fox']]])

        expect:
        toml(config) == '''
[foo]
bar = 4

[foo.baz]
foo = 123
bar = [1, 2, {name = 'fox'}]
'''
    }

    def 'empty key avoidance'() {
        given:
        def config = new Configuration('', '', '')
        config.registerTable('foo.bar') // this table is discarded
        config.put('foo.bar', 4)
        config.registerTable('foo.baz')
        config.put('foo.baz', ['foo': 123, 'bar': [1, 2, ['name': 'fox']]])

        expect:
        toml(config) == '''foo.bar = 4

[foo.baz]
foo = 123
bar = [1, 2, {name = 'fox'}]
'''
    }
}
