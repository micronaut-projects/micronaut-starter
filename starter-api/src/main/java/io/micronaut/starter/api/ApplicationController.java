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

    /**
     * Default constructor.
     * @param featureOperations The feature operations.
     */
    public ApplicationController(FeatureOperations featureOperations) {
        this.featureOperations = featureOperations;
    }

    /**
     * List the application types.
     * @param info the request info
     * @return The types
     */
    @Override
    @Get("/application-types")
    public ApplicationTypeList list(RequestInfo info) {
        List<ApplicationTypeDTO> types = Arrays.stream(ApplicationTypes.values())
                .map(type -> typeToDTO(type, info, false))
                .collect(Collectors.toList());
        ApplicationTypeList applicationTypeList = new ApplicationTypeList(types);
        applicationTypeList.addLink(
                Relationship.SELF,
                info.self()
        );
        return applicationTypeList;
    }

    /**
     * Get a specific application type.
     * @param type The type
     * @param info The request info
     * @return The type
     */
    @Override
    @Get("/application-types/{type}")
    public ApplicationTypeDTO getType(ApplicationTypes type, RequestInfo info) {
        return typeToDTO(type, info, true);
    }

    /**
     * List the type features.
     * @param type The features
     * @param requestInfo The request info
     * @return The features
     */
    @Override
    @Get("/application-types/{type}/features")
    public FeatureList features(ApplicationTypes type, RequestInfo requestInfo) {
        FeatureList featureList = new FeatureList(featureOperations.getFeatures(type));
        featureList.addLink(
                Relationship.SELF,
                requestInfo.self()
        );
        return featureList;
    }

    private ApplicationTypeDTO typeToDTO(ApplicationTypes type, RequestInfo requestInfo, boolean includeFeatures) {
        List<FeatureDTO> features = includeFeatures ? featureOperations.getFeatures(type) : Collections.emptyList();
        ApplicationTypeDTO dto = new ApplicationTypeDTO(type, features);
        dto.addLink(
                Relationship.CREATE,
                requestInfo.link(Relationship.CREATE, type)
        );
        dto.addLink(
                Relationship.PREVIEW,
                requestInfo.link(Relationship.PREVIEW, type)
        );
        dto.addLink(
                Relationship.SELF,
                requestInfo.link(type)
        );
        return dto;
    }

}
