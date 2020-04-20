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
package io.micronaut.starter.api.bind;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.env.Environment;
import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.bind.binders.TypedRequestArgumentBinder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.starter.api.RequestInfo;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * Binds the Server URL.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Singleton
public class RequestInfoArgumentBinder implements TypedRequestArgumentBinder<RequestInfo> {

    private static final Argument<RequestInfo> TYPE = Argument.of(RequestInfo.class);

    private final Provider<EmbeddedServer> embeddedServerProvider;

    /**
     * Default constructor.
     * @param embeddedServerProvider The embedded server provider
     */
    public RequestInfoArgumentBinder(@Nullable Provider<EmbeddedServer> embeddedServerProvider) {
        this.embeddedServerProvider = embeddedServerProvider;
    }

    @Override
    public Argument<RequestInfo> argumentType() {
        return TYPE;
    }

    @Override
    public BindingResult<RequestInfo> bind(ArgumentConversionContext<RequestInfo> context, HttpRequest<?> source) {
        String url = resolveUrl(source);
        return () -> Optional.of(new RequestInfo(url, source.getPath()));
    }

    private String resolveUrl(HttpRequest<?> request) {
        String url;
        String hostname = System.getenv(Environment.HOSTNAME);
        String host;
        InetSocketAddress serverAddress = request.getServerAddress();
        if (hostname != null) {
            host = hostname;
        } else {
            hostname = request.getUri().getHost();
            if (hostname != null) {
                host = hostname;
            } else {
                if (embeddedServerProvider != null) {
                    host = embeddedServerProvider.get().getHost();
                } else {
                    host = SocketUtils.LOCALHOST;
                }
            }
        }
        int port = serverAddress.getPort();
        if (port > -1 && port != 80) {
            host = host + ":" + port;
        }
        url = (request.isSecure() ? "https" : "http") + "://" + host ;
        return url;
    }
}
