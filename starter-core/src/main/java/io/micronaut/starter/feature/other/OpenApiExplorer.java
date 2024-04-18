/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.other;

import java.util.ArrayList;
import java.util.List;

import io.micronaut.starter.feature.InterceptUrlMap;
import io.micronaut.starter.feature.staticResources.ContributingStaticResources;
import io.micronaut.starter.feature.staticResources.StaticResource;

import jakarta.inject.Singleton;

@Singleton
public class OpenApiExplorer extends OpenApiView implements ContributingStaticResources {

    public static final String NAME = "openapi-explorer";

    public OpenApiExplorer(OpenApi openApiFeature) {
        super(openApiFeature);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "OpenAPI Explorer View";
    }

    @Override
    public String getDescription() {
        return "Adds and enables OpenAPI Explorer view for OpenAPI";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/Authress-Engineering/openapi-explorer";
    }

    @Override
    public String getMicronautDocumentation() {
            return "https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#openapiExplorer";
    }

    @Override
    public List<StaticResource> staticResources() {
        List<StaticResource> result = new ArrayList<>(super.staticResources());
        result.add(new StaticResource("openapi-explorer", "/openapi-explorer/**", "classpath:META-INF/swagger/views/openapi-explorer"));
        return result;
    }

    @Override
    public List<InterceptUrlMap> interceptUrlMaps() {
        List<InterceptUrlMap> result = new ArrayList<>(super.interceptUrlMaps());
        result.add(InterceptUrlMap.anonymousAcccess("/openapi-explorer/**"));
        return result;
    }
}
