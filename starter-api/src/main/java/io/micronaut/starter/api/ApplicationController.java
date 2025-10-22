/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api;

import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.api.starterApi;
import io.micronaut.starter.util.VersionInfo;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main interface on the starter API.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Micronaut Launch",
                version = "${api.version}",
                description = "API for Creating Micronaut Applications",
                license = @License(name = "Apache 2.0")
        )
)
@Controller
public class ApplicationController implements ApplicationTypeOperations {

    private final FeatureOperations featureOperations;
    private final StarterConfiguration configuration;
    private final MessageSource messageSource;

    /**
     * Default constructor.
     * @param featureOperations The feature operations.
     * @param configuration The starter configuration
     * @param messageSource The message source
     */
    public ApplicationController(FeatureOperations featureOperations, StarterConfiguration configuration, MessageSource messageSource) {
        this.featureOperations = featureOperations;
        this.configuration = configuration;
        this.messageSource = messageSource;
    }

    /**
     * Information about this instance.
     * @return Information about this instance.
     */
    @Get("/versions")
    VersionDTO getInfo(@Parameter(hidden = true) RequestInfo info) {
        return new VersionDTO()
                .addLink(Relationship.SELF, info.self());
    }

    @Hidden
    @Get
    @ApiResponse(
            responseCode = "200",
            description = "A textual description of the API",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    @Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    HttpResponse<?> redirectToUi(HttpRequest<?> request, @Parameter(hidden = true) RequestInfo info) {
        URI redirectURI = configuration.getRedirectUri().orElse(null);
        boolean acceptsHtml = request.accept().stream()
                .anyMatch(mediaType -> mediaType.equals(MediaType.TEXT_HTML_TYPE));
        if (acceptsHtml && redirectURI != null) {
            return HttpResponse.permanentRedirect(redirectURI);
        }
        return home(request, info).contentType(MediaType.TEXT_PLAIN_TYPE);
    }

    /**
     * Provides a description of the API.
     * @param request The request
     * @return A description of the API.
     */
    MutableHttpResponse<Writable> home(HttpRequest<?> request, @Parameter(hidden = true) RequestInfo info) {
        return HttpResponse.ok(new Writable() {
            @Override
            public void writeTo(Writer out) {
                // no-op
            }
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) {
                new RockerWritable(new starterApi()
                        .serverURL(info.getServerURL())
                        .micronautVersion(VersionInfo.getMicronautVersion()))
                        .write(outputStream);
            }
        });
    }

    /**
     * List the application types.
     * @param info the request info
     * @return The types
     */
    @Override
    @Get("/application-types")
    public ApplicationTypeList list(RequestInfo info) {
        List<ApplicationTypeDTO> types = Arrays.stream(ApplicationType.values())
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
    public ApplicationTypeDTO getType(ApplicationType type, RequestInfo info) {
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
    public FeatureList features(ApplicationType type, RequestInfo requestInfo) {
        FeatureList featureList = new FeatureList(featureOperations.getFeatures(requestInfo.getLocale(), type));
        featureList.addLink(
                Relationship.SELF,
                requestInfo.self()
        );
        return featureList;
    }

    private ApplicationTypeDTO typeToDTO(ApplicationType type, RequestInfo requestInfo, boolean includeFeatures) {
        List<FeatureDTO> features = includeFeatures ? featureOperations.getFeatures(requestInfo.getLocale(), type) : Collections.emptyList();
        features.forEach(featureDTO -> featureDTO.addLink(
                Relationship.DIFF,
                requestInfo.link("/diff/" + type.getName() + "/feature/" + featureDTO.getName())
        ));
        ApplicationTypeDTO dto = new ApplicationTypeDTO(
                type, features, messageSource, MessageSource.MessageContext.of(requestInfo.getLocale())
        );
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
