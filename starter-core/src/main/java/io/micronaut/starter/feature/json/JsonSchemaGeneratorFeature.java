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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.build.gradle.templates.jsonschemaExtension;
import io.micronaut.starter.feature.validator.MicronautValidationFeature;
import io.micronaut.starter.feature.validator.ValidationFeature;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.json.schema.generator.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class JsonSchemaGeneratorFeature implements Feature {

    public static final String NAME = "json-schema-generator";
    private static final String GRADLE_PLUGIN_ID = "io.micronaut.jsonschema";
    private static final String GRADLE_PLUGIN_ARTIFACT_ID = "micronaut-gradle-plugin";

    private final MicronautValidationFeature validationFeature;

    public JsonSchemaGeneratorFeature(MicronautValidationFeature validationFeature) {
        this.validationFeature = validationFeature;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(ValidationFeature.class, validationFeature);
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "JSON Schema Generator";
    }

    @Override
    public String getDescription() {
        return "Adds capability to generate source classes from JSON schema definitions to a Micronaut Application.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/index.html#generator";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            addGradlePlugin(generatorContext);
        } else {
            addMavenPlugin(generatorContext);
        }
    }

    protected void addGradlePlugin(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(GradlePlugin.builder()
                .id(GRADLE_PLUGIN_ID)
                .lookupArtifactId(GRADLE_PLUGIN_ARTIFACT_ID)
                .extension(new RockerWritable(jsonschemaExtension.template()))
                .build());
    }

    protected void addMavenPlugin(GeneratorContext generatorContext) {
        String schemaUrl = "https://raw.githubusercontent.com/micronaut-projects/micronaut-json-schema/refs/heads/1.5.x/test-suite-generator-java/src/test/resources/animal.schema.json";
        BuildProperties buildProperties = generatorContext.getBuildProperties();
        buildProperties.put("micronaut.jsonschema.generator.enabled", StringUtils.TRUE);
        buildProperties.put("micronaut.jsonschema.generator.input-url", schemaUrl);
        buildProperties.put("micronaut.jsonschema.generator.outputPackageName", "com.example.animals");
    }
}
