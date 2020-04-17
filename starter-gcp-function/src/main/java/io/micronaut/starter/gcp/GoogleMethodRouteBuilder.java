package io.micronaut.starter.gcp;

import io.micronaut.context.ExecutionHandleLocator;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.context.ServerContextPathProvider;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.ExecutableMethod;
import io.micronaut.web.router.AnnotatedMethodRouteBuilder;
import io.micronaut.web.router.UriRoute;

import javax.inject.Singleton;

@Singleton
@Internal
@Replaces(AnnotatedMethodRouteBuilder.class)
class GoogleMethodRouteBuilder extends AnnotatedMethodRouteBuilder {
    private final ServerContextPathProvider contextPathProvider;

    /**
     * @param executionHandleLocator The execution handler locator
     * @param uriNamingStrategy      The URI naming strategy
     * @param conversionService      The conversion service
     * @param contextPathProvider    The context path provider
     */
    public GoogleMethodRouteBuilder(
            ExecutionHandleLocator executionHandleLocator,
            UriNamingStrategy uriNamingStrategy,
            ConversionService<?> conversionService,
            ServerContextPathProvider contextPathProvider) {
        super(executionHandleLocator, uriNamingStrategy, conversionService);
        this.contextPathProvider = contextPathProvider;
    }

    @Override
    protected UriRoute buildBeanRoute(String httpMethodName, HttpMethod httpMethod, String uri, BeanDefinition<?> beanDefinition, ExecutableMethod<?, ?> method) {
        String cp = contextPathProvider.getContextPath();
        if (cp != null) {
            uri = StringUtils.prependUri(cp, uri);
        }
        return super.buildBeanRoute(httpMethodName, httpMethod, uri, beanDefinition, method);
    }
}

