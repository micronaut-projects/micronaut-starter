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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.server.EmbeddedServer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main interface on the the starter API.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Micronaut Starter",
                version = "1.0",
                description = "API for Creating Micronaut Applications",
                license = @License(name = "Apache 2.0")
        )
)
@Controller("/")
public class ApplicationController implements ApplicationTypeOperations {

    private final FeatureOperations featureOperations;
    private final EmbeddedServer embeddedServer;

    /**
     * Default constructor.
     * @param featureOperations The feature operations.
     * @param embeddedServer The server if available
     */
    public ApplicationController(FeatureOperations featureOperations, @Nullable EmbeddedServer embeddedServer) {
        this.featureOperations = featureOperations;
        this.embeddedServer = embeddedServer;
    }

    /**
     * List the application types.
     * @param request The request
     * @return The types
     */
    @Override
    @Get("/application-types")
    public List<ApplicationTypeDTO> list(HttpRequest<?> request) {
        return Arrays.stream(ApplicationTypes.values())
                .map(type -> typeToDTO(type, request, false))
                .collect(Collectors.toList());
    }

    /**
     * Get a specific application type
     * @param type The type
     * @param request The request
     * @return The type
     */
    @Override
    @Get("/application-types/{type}")
    public ApplicationTypeDTO getType(ApplicationTypes type, HttpRequest<?> request) {
        return typeToDTO(type, request, true);
    }

    /**
     * List the type features.
     * @param type The features
     * @return The features
     */
    @Override
    @Get("/application-types/{type}/features")
    public List<FeatureDTO> features(ApplicationTypes type) {
        return featureOperations.getFeatures(type);
    }

    private ApplicationTypeDTO typeToDTO(ApplicationTypes type, HttpRequest<?> request, boolean includeFeatures) {
        List<FeatureDTO> features = includeFeatures ? featureOperations.getFeatures(type) : Collections.emptyList();
        ApplicationTypeDTO dto = new ApplicationTypeDTO(type, features);
        createLink(request, type, "create", dto);
        createLink(request, type, "preview", dto);
        return dto;
    }

    private void createLink(HttpRequest<?> request, ApplicationTypes type, String rel, ApplicationTypeDTO dto) {
        LinkDTO link;
        if (embeddedServer != null) {
            String url = embeddedServer.getURL().toString();
            if (!url.endsWith("/")) {
                url += "/";
            }
            link = new LinkDTO((url + rel + "/" + type + "/{name}"));
        } else {
            InetSocketAddress serverAddress = request.getServerAddress();
            String host = serverAddress.getHostString();
            int port = serverAddress.getPort();
            if (port > -1) {
                host = host + ":" + port;
            }
            link = new LinkDTO((request.isSecure() ? "https" : "http") + "://" + host + "/" + rel + "/" + type + "/{name}");
        }
        dto.addLink(rel, link);
    }
}
