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

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.core.io.Writable;
import io.micronaut.core.version.VersionUtils;
import io.micronaut.discovery.event.ServiceReadyEvent;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.api.starterApi;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
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
public class ApplicationController implements ApplicationTypeOperations, ApplicationEventListener<ServiceReadyEvent> {

    private final FeatureOperations featureOperations;
    private Map<String, String> metadata;

    /**
     * Default constructor.
     * @param featureOperations The feature operations.
     */
    public ApplicationController(FeatureOperations featureOperations) {
        this.featureOperations = featureOperations;
    }

    /**
     * Information about this instance.
     * @param request The request
     * @param info The info
     * @return Information about this instance.
     */
    @Get("/instance/version")
    VersionDTO getInfo(HttpRequest<?> request, @Parameter(hidden = true) RequestInfo info) {
        return new VersionDTO(request.getUri().toString(), request.getServerAddress());
    }

    /**
     * Metadata about this instance.
     * @return Information about this instance.
     */
    @Get("/instance/metadata")
    Map<String, String> getMetadata() {
        return this.metadata;
    }

    /**
     * Provides a description of the API.
     * @param request The request
     * @return A description of the API.
     */
    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    HttpResponse<Writable> home(HttpRequest<?> request, @Parameter(hidden = true) RequestInfo info) {
        Collection<MediaType> accept = request.accept();
        if (accept.contains(MediaType.TEXT_HTML_TYPE)) {
            return HttpResponse.permanentRedirect(URI.create("https://micronaut.io/start"));
        } else {
            return HttpResponse.ok(new Writable() {

                @Override
                public void writeTo(Writer out) {
                    // no-op
                }

                @Override
                public void writeTo(OutputStream outputStream, @Nullable Charset charset) {
                    new RockerTemplate("home", new starterApi()
                            .serverURL(info.getServerURL())
                            .micronautVersion(VersionUtils.MICRONAUT_VERSION)).write(
                        outputStream
                    );
                }
            });
        }
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
        FeatureList featureList = new FeatureList(featureOperations.getFeatures(type));
        featureList.addLink(
                Relationship.SELF,
                requestInfo.self()
        );
        return featureList;
    }

    private ApplicationTypeDTO typeToDTO(ApplicationType type, RequestInfo requestInfo, boolean includeFeatures) {
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

    @Override
    public void onApplicationEvent(ServiceReadyEvent event) {
        this.metadata = event.getSource().getMetadata().asMap();
    }
}
