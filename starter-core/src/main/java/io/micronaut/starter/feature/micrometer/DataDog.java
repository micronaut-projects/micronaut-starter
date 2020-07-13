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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.other.Management;

import javax.inject.Singleton;

@Singleton
public class DataDog extends MicrometerFeature {

    public DataDog(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return "micrometer-datadog";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micrometer metrics (w/ Datadog reporter)";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".datadog.enabled", true);
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".datadog.apiKey", "${DATADOG_APIKEY}");
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".datadog.step", "PT1M");
    }
}
