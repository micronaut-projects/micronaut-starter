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
package io.micronaut.starter.feature.opensearch;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.json.JacksonDatabindFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

@Singleton
public class OpenSearchRestClient extends OpenSearchFeature {

    public static final String NAME = "opensearch-restclient";

    private final JacksonDatabindFeature jacksonDatabindFeature;

    public OpenSearchRestClient(
            TestContainers testContainers,
            TestResources testResources,
            JacksonDatabindFeature jacksonDatabindFeature
    ) {
        super(testContainers, testResources);
        this.jacksonDatabindFeature = jacksonDatabindFeature;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "OpenSearch REST Client";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(JacksonDatabindFeature.class)) {
            featureContext.addFeature(jacksonDatabindFeature);
        }
    }

    @Override
    public String getDescription() {
        return "Adds support for OpenSearch RestClient-based transport";
    }
}
