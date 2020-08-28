package io.micronaut.starter.api

import io.micronaut.context.ApplicationContext
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class EmbeddedServerSpecification extends Specification implements ConfigurationFixture {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, configuration)

    @Shared
    @AutoCleanup
    ApplicationContext applicationContext = embeddedServer.applicationContext

    @Shared
    HttpClient httpClient = applicationContext.createBean(HttpClient, embeddedServer.URL)

    @Shared
    BlockingHttpClient client = httpClient.toBlocking();
}
