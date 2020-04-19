/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.runtime.server.EmbeddedServer;

import javax.inject.Singleton;
import java.net.InetSocketAddress;

/**
 * Default URL resolver.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Singleton
public class DefaultUrlResolver implements ServerUrlResolver {
    private final EmbeddedServer embeddedServer;

    /**
     * Default constructor.
     * @param embeddedServer The embedded server
     */
    public DefaultUrlResolver(@Nullable EmbeddedServer embeddedServer) {
        this.embeddedServer = embeddedServer;
    }

    @Override
    public String resolveUrl(HttpRequest<?> request) {
        String url;
        if (embeddedServer != null) {
            url = embeddedServer.getURL().toString();
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
        } else {
            InetSocketAddress serverAddress = request.getServerAddress();
            String host = serverAddress.getHostString();
            int port = serverAddress.getPort();
            if (port > -1) {
                host = host + ":" + port;
            }
            url = (request.isSecure() ? "https" : "http") + "://" + host ;
        }
        return url;
    }
}
