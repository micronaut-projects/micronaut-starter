package io.micronaut.starter.api;

import io.micronaut.core.io.Writable;
import io.micronaut.http.annotation.Get;

import javax.annotation.Nullable;
import java.util.List;

public interface CreateOperations {

    @Get(uri = "/app/{name}{?features}", produces = "application/zip")
    Writable createApp(String name, @Nullable List<String> features);

}
