/*
 * Copyright 2017-2020 original authors
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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.other.Management;
import jakarta.inject.Singleton;

@Singleton
public class Atlas extends MicrometerFeature {

    public Atlas(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-atlas";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micrometer metrics (w/ Atlas reporter)";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".atlas.enabled", true);
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".atlas.uri", "http://localhost:7101/api/v1/publish");
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".atlas.step", "PT1M");
        generatorContext.addDependency(Dependency.builder()
                .groupId(getDependencyGroupName())
                .artifactId(getDependencyName())
                .compile());
    }
}
