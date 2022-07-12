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
package io.micronaut.starter.feature.testresources;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.TestContainers;

public abstract class EaseTestingFeature implements Feature  {
    private final TestResources testResources;
    private final TestContainers testContainers;

    protected EaseTestingFeature() {
        this.testContainers = null;
        this.testResources = null;
    }

    protected EaseTestingFeature(TestContainers testContainers,
                                 TestResources testResources) {
        this.testContainers = testContainers;
        this.testResources = testResources;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(TestResources.class) && testResources != null) {
            featureContext.addFeature(testResources);
        }
    }
}
