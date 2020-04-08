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
package io.micronaut.starter.feature.micrometer;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.other.Management;

public abstract class MicrometerFeature implements OneOfFeature {

    protected final String EXPORT_PREFIX = "micronaut.metrics.export";

    private final Core core;
    private final Management management;

    public MicrometerFeature(Core core, Management management) {
        this.core = core;
        this.management = management;
    }

    @Override
    public Class<?> getFeatureClass() {
        return MicrometerFeature.class;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(core);
        if (featureContext.getFeatures().stream().noneMatch(f -> f instanceof Management)) {
            featureContext.addFeature(management);
        }
    }

    protected void addExportEnabled(CommandContext ctx, String registry, boolean enabled) {
        ctx.getConfiguration().put("micronaut.metrics.export." + registry + ".enabled", enabled);
    }
}
