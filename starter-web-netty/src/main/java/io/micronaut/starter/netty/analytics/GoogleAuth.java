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
package io.micronaut.starter.netty.analytics;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@Filter(serviceId = "analytics", patterns = Filter.MATCH_ALL_PATTERN)
@Requires(env = Environment.GOOGLE_COMPUTE)
public class GoogleAuth implements HttpClientFilter, AutoCloseable {
    private final RxHttpClient authClient;

    public GoogleAuth() {
        try {
            this.authClient = RxHttpClient.create(new URL("http://metadata"));
        } catch (MalformedURLException e) {
            throw new HttpClientException("Cannot create Google Auth Client: " + e.getMessage(), e);
        }
    }

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        Flowable<String> token = Flowable.fromCallable(() -> encodeURI(request))
        .flatMap(authURI -> authClient.retrieve(HttpRequest.GET(authURI).header(
                "Metadata-Flavor", "Google"
        )));

        return token.flatMap(t -> chain.proceed(request.bearerAuth(t)));
    }

    protected String encodeURI(MutableHttpRequest<?> request) throws UnsupportedEncodingException {
        return "/computeMetadata/v1/instance/service-accounts/default/identity?audience=" +
                URLEncoder.encode(request.getUri().toString(), "UTF-8");
    }

    @Override
    @PreDestroy
    public void close() {
        authClient.close();
    }
}
