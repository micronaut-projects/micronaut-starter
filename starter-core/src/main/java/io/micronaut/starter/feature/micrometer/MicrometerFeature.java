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
package io.micronaut.starter.feature.micrometer;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.util.NameUtils;

public abstract class MicrometerFeature implements Feature, MicronautServerDependent {

    protected final String EXPORT_PREFIX = "micronaut.metrics.export";

    private final Core core;
    private final Management management;

    public MicrometerFeature(Core core, Management management) {
        this.core = core;
        this.management = management;
    }

    @Override
    public String getName() {
        return "micrometer-" + getImplementationName();
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Core.class)) {
            featureContext.addFeature(core);
        }
        if (!featureContext.isPresent(Management.class)) {
            featureContext.addFeature(management);
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        doApply(generatorContext);
        generatorContext.addDependency(Dependency.builder()
                .groupId(getGroupId())
                .artifactId(getArtifactId())
                .compile());
    }

    @Override
    public String getCategory() {
        return Category.MANAGEMENT;
    }

    @Override
    public String getTitle() {
        return NameUtils.getNaturalName(io.micronaut.core.naming.NameUtils.dehyphenate(getName()));
    }

    protected String getGroupId() {
        return "io.micronaut.micrometer";
    }

    protected String getArtifactId() {
        return "micronaut-micrometer-registry-" + getImplementationName();
    }

    protected abstract String getImplementationName();

    protected abstract void doApply(GeneratorContext generatorContext);

}
