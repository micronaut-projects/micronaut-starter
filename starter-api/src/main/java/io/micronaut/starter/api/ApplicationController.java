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
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

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
    private final ServerUrlResolver resolver;

    /**
     * Default constructor.
     * @param featureOperations The feature operations.
     * @param resolver The server url resolver
     */
    public ApplicationController(FeatureOperations featureOperations, ServerUrlResolver resolver) {
        this.featureOperations = featureOperations;
        this.resolver = resolver;
    }

    /**
     * List the application types.
     * @param request The request
     * @return The types
     */
    @Override
    @Get("/application-types")
    public ApplicationTypeList list(HttpRequest<?> request) {
        List<ApplicationTypeDTO> types = Arrays.stream(ApplicationTypes.values())
                .map(type -> typeToDTO(type, request, false))
                .collect(Collectors.toList());
        return new ApplicationTypeList(types);
    }

    /**
     * Get a specific application type.
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
    public FeatureList features(ApplicationTypes type) {
        return new FeatureList(featureOperations.getFeatures(type));
    }

    private ApplicationTypeDTO typeToDTO(ApplicationTypes type, HttpRequest<?> request, boolean includeFeatures) {
        List<FeatureDTO> features = includeFeatures ? featureOperations.getFeatures(type) : Collections.emptyList();
        ApplicationTypeDTO dto = new ApplicationTypeDTO(type, features);
        Linkable.addLink(resolver, request, type, "create", dto);
        Linkable.addLink(resolver, request, type, "preview", dto);
        dto.addLink("self", new LinkDTO(resolver.resolveUrl(request) + request.getUri()));
        return dto;
    }

}
