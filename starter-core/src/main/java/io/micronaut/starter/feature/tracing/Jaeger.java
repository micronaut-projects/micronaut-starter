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
package io.micronaut.starter.feature.tracing;

import io.micronaut.starter.application.generator.GeneratorContext;

import javax.inject.Singleton;

@Singleton
public class Jaeger implements TracingFeature {

    @Override
    public String getName() {
        return "tracing-jaeger";
    }

    @Override
    public String getTitle() {
        return "Jaeger Tracing";
    }

    @Override
    public String getDescription() {
        return "Adds support for distributed tracing with Jaeger (https://www.jaegertracing.io)";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("tracing.jaeger.enabled", true);
        generatorContext.getConfiguration().put("tracing.jaeger.sampler.probability", 0.1);
    }
}
