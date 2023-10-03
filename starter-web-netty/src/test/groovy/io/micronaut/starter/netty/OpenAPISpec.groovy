package io.micronaut.starter.netty

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class OpenAPISpec extends Specification {

    @Inject
    EmbeddedServer server

    @Client("/")
    @Inject
    HttpClient client

    void "test swagger-ui"() {
        when:
        def response = server.getURI().resolve("/swagger-ui").toURL().text

        List<String> matches = (response =~ /(?:src|href)=['"]([^'"]+)['"]/).collect { it[1] as String }
        String yml = (response =~ /['"]([^'"]+\.yml)['"]/)[0][1]

        then:
        matches.size()
        yml

        and:
        server.getURI().resolve(yml).toURL().text.contains("title: Micronaut Launch")

        and:
        matches.each {
            assert client.toBlocking().exchange(it).status() == HttpStatus.OK
        }
    }

    void "test rapidoc"() {
        when:
        def response = server.getURI().resolve("/rapidoc").toURL().text

        List<String> matches = (response =~ /(?:src|href)=['"]([^'"]+)['"]/).collect { it[1] as String }
        String yml = (response =~ /['"]([^'"]+\.yml)['"]/)[0][1]

        then:
        matches.size()
        yml

        and:
        server.getURI().resolve(yml).toURL().text.contains("title: Micronaut Launch")

        and:
        matches.each {
            assert client.toBlocking().exchange(it).status() == HttpStatus.OK
        }
    }
}
