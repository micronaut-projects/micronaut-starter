package io.micronaut.starter.api

import io.micronaut.core.io.Writable
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@MicronautTest
class CreateControllerSpec extends Specification {

    @Inject
    CreateClient client

    void "test default create app command"() {
        when:
        def bytes = client.createApp("test", Collections.emptyList())
        def zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes))
        def zipEntry = zipInputStream.getNextEntry()

        then:
        zipEntry != null

    }

    @Client('/create')
    static interface CreateClient {
        @Get(uri = "/app/{name}{?features}", consumes = "application/zip")
        byte[] createApp(String name, List<String> features);
    }
}
