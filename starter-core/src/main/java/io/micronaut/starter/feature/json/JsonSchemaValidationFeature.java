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
package io.micronaut.starter.feature.json;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

@Singleton
public class JsonSchemaValidationFeature implements Feature {

    public static final String NAME = "json-schema-validation";

    private static final Dependency JSON_SCHEMA_VALIDATION_TEST_DEPENDENCY = MicronautDependencyUtils.jsonSchemaDependency()
            .artifactId("micronaut-json-schema-validation")
            .test()
            .build();

    private final JsonSchemaFeature jsonSchemaFeature;

    public JsonSchemaValidationFeature(JsonSchemaFeature jsonSchemaFeature) {
        this.jsonSchemaFeature = jsonSchemaFeature;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "JSON Schema Validation";
    }

    @Override
    public String getDescription() {
        return "Adds JSON Schema Validation to a Micronaut Application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(JSON_SCHEMA_VALIDATION_TEST_DEPENDENCY);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(JsonSchemaFeature.class, jsonSchemaFeature);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/index.html#validation";
    }
}
