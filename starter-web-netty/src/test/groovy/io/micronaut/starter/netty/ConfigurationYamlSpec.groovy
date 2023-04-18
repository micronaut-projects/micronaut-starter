package io.micronaut.starter.netty

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class ConfigurationYamlSpec extends Specification {

    @Inject
    ApplicationContext applicationContext

    void "Yaml configuration is loadable"() {
        when:
        Optional<List<String>> listOptional = applicationContext.getProperty("gcp.http.client.auth.patterns", Argument.listOf(String))

        then:
        listOptional.isPresent()

        when:
        List<String> l = listOptional.get()

        then:
        ['/analytics/**'] == l
    }
}
