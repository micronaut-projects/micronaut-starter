package io.micronaut.starter.servlet

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Ignore
import spock.lang.Specification

@MicronautTest
class OpenAPISpec extends Specification {

    @Inject
    EmbeddedServer server

    @Client("/")
    @Inject
    HttpClient client

    @Ignore
    void "test swagger-ui links"() {
        when:
        String response = client.toBlocking().retrieve("/swagger-ui/index.html", String)
        List<String> matches = (response =~ /(?:src|href)=['"]([^'"]+)['"]/).collect { it[1] as String }

        then:
        matches.size()

        and:
        matches.each {
            assert client.toBlocking().exchange(it).status() == HttpStatus.OK
        }
    }

    void "test swagger-ui"() {
        when:
        String response = client.toBlocking().retrieve("/swagger-ui/index.html", String)
        String yml = (response =~ /['"]([^'"]+\.yml)['"]/)[0][1]

        then:
        yml

        and:
        server.getURI().resolve(yml).toURL().text.contains("title: Micronaut Launch")
    }

    @Ignore
    void "test rapidoc links"() {
        when:
        String response = client.toBlocking().retrieve("/rapidoc/index.html", String)

        List<String> matches = (response =~ /(?:src|href)=['"]([^'"]+)['"]/).collect { it[1] as String }

        then:
        matches.size()

        and:
        matches.each {
            assert client.toBlocking().exchange(it).status() == HttpStatus.OK
        }
    }

    void "test rapidoc"() {
        when:
        String response = client.toBlocking().retrieve("/rapidoc/index.html", String)

        String yml = (response =~ /['"]([^'"]+\.yml)['"]/)[0][1]

        then:
        yml

        and:
        server.getURI().resolve(yml).toURL().text.contains("title: Micronaut Launch")
    }
}
