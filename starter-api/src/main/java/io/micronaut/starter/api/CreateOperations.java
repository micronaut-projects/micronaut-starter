package io.micronaut.starter.api;

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Operations for creating different kinds of applications.
 *
 * @author graemerocher
 * @since 1.0
 */
public interface CreateOperations {

    /**
     * Creates a Micronaut application.
     * @param name The application name
     * @param features The features desired
     * @return A writable that emits a zip file
     */
    @Get(uri = "/app/{name}{?features}", produces = "application/zip")
    HttpResponse<Writable> createApp(String name, @Nullable List<String> features);

    /**
     * Creates a Micronaut Function application.
     * @param name The application name
     * @param features The features desired
     * @return A writable that emits a zip file
     */
    @Get(uri = "/function/{name}{?features}", produces = "application/zip")
    HttpResponse<Writable> createFunction(String name, @Nullable List<String> features);

    /**
     * Creates a Micronaut GRPC application.
     * @param name The application name
     * @param features The features desired
     * @return A writable that emits a zip file
     */
    @Get(uri = "/grpc/{name}{?features}", produces = "application/zip")
    HttpResponse<Writable> createGrpcApp(String name, @Nullable List<String> features);

    /**
     * Creates a Micronaut Messaging application.
     * @param name The application name
     * @param features The features desired
     * @return A writable that emits a zip file
     */
    @Get(uri = "/messaging/{name}{?features}", produces = "application/zip")
    HttpResponse<Writable> createMessagingApp(String name, @Nullable List<String> features);

    /**
     * Creates a Micronaut CLI application.
     * @param name The application name
     * @param features The features desired
     * @return A writable that emits a zip file
     */
    @Get(uri = "/cli/{name}{?features}", produces = "application/zip")
    HttpResponse<Writable> createCliApp(String name, @Nullable List<String> features);

}
