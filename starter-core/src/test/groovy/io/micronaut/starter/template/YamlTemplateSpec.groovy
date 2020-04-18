package io.micronaut.starter.template

import spock.lang.Specification

class YamlTemplateSpec extends Specification {

    void "test yaml output"() {
        Map<String, Object> config = [:]
        config.put("datasources.default.url", "dbURL")
        config.put("datasources.default.className", "h2")
        config.put("jpa.default.properties.hibernate.hbm2ddl", "auto")
        config.put("micronaut.application.name", "foo")

        YamlTemplate template = new YamlTemplate("abc", config)
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        template.write(baos)

        expect:
        //single value nested keys get collapsed
        //micronaut. ignores that rule
        baos.toString() == """datasources:
  default:
    url: dbURL
    className: h2
jpa.default.properties.hibernate.hbm2ddl: auto
micronaut:
  application:
    name: foo
"""
    }
}
