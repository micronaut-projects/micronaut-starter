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
package io.micronaut.starter.feature.openapi;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.openapi.template.resourceConfigJson;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class OpenApi implements Feature {

    @Override
    public String getName() {
        return "openapi";
    }

    @Override
    public String getTitle() {
        return "OpenAPI Support";
    }

    @Override
    public String getDescription() {
        return "Adds support for OpenAPI (Swagger)";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            Map.Entry<String, String> dependencyVersion = VersionInfo.getDependencyVersion("micronaut.openapi");
            generatorContext.getBuildProperties().put(
                    dependencyVersion.getKey(),
                    dependencyVersion.getValue()
            );
        }

        if (generatorContext.isFeaturePresent(GraalVM.class)) {
            generatorContext.addTemplate("resourceConfigJson",
                    new RockerTemplate("src/main/resources/META-INF/native-image/{packageName}/{name}-application/resource-config.json", resourceConfigJson.template(generatorContext.getProject())
                    )
            );
        }
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.openapis.org";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html";
    }
}
