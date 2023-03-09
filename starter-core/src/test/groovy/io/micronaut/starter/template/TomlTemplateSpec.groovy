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

    def 'array table'() {
        given:
        def config = new Configuration('', '', '')
        config.put('foo', [['buzz': 'bizz'], ['a': 'b', 'c': [['d': 'e'], ['f': 'g']], 'h': ['i': 'j'], 'k': ['l': 'm', 'n': 'o']]])

        expect:
        toml(config) == '''\

[[foo]]
buzz = 'bizz'

[[foo]]
a = 'b'
h.i = 'j'

[[foo.c]]
d = 'e'

[[foo.c]]
f = 'g'

[foo.k]
l = 'm'
n = 'o'
'''
    }
}
