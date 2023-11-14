/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.microstream;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class MicroStreamCache implements MicroStreamFeature {

    public static final String NAME = "microstream-cache";
    public static final String ARTIFACT_ID_MICRONAUT_MICROSTREAM_CACHE = "micronaut-microstream-cache";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MicroStream Cache";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for using MicroStream as a cache";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.CACHE;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("microstream.cache.my-cache.key-type", "java.lang.Integer");
        generatorContext.getConfiguration().put("microstream.cache.my-cache.value-type", "java.lang.String");
        generatorContext.addDependency(MicronautDependencyUtils.microstreamDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_MICROSTREAM_CACHE)
                .compile()
        );
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-microstream/latest/guide/#cache";
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://docs.microstream.one/manual/cache/index.html";
    }
}
