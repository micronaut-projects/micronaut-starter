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

import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.bind.binders.TypedRequestArgumentBinder;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.StarterConfiguration;

import javax.inject.Singleton;
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
    private final StarterConfiguration configuration;

    /**
     * Default constructor.
     * @param configuration The configuration
     */
    public RequestInfoArgumentBinder(StarterConfiguration configuration) {
        this.configuration = configuration;
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

        String cp = configuration.getPath().orElse("");
        String url = configuration.getUrl().map(Object::toString).orElse(null);
        if (url != null) {
            if (url.startsWith("https://")) {
                return url + cp;
            } else {
                return "https://" + url + cp;
            }
        } else {
           String hostname = request.getUri().getHost();
           String host;
            if (hostname != null) {
                host = hostname;
            } else {
                host = SocketUtils.LOCALHOST;
            }
            int port = request.getServerAddress().getPort();
            if (port > -1 && port != 80) {
                host = host + ":" + port;
            }
            url = (request.isSecure() ? "https" : "http") + "://" + host + cp;
        }

        return url;
    }
}
