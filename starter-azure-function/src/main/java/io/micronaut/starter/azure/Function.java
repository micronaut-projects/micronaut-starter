package io.micronaut.starter.azure;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import io.micronaut.azure.function.http.AzureHttpFunction;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function extends AzureHttpFunction {
    @FunctionName("MicronautStarterTrigger")
    public HttpResponseMessage invoke(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    route = "{*route}",
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<byte[]>> request,
            final ExecutionContext context) {
        return super.route(request, context);
    }
}
