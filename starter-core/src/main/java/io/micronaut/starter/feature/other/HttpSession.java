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
package io.micronaut.starter.feature.other;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.redis.RedisLettuce;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class HttpSession implements Feature  {

    @NonNull
    @Override
    public String getName() {
        return "http-session";
    }

    @Override
    public String getTitle() {
        return "HTTP Sessions";
    }

    @Override
    public String getDescription() {
        return "Adds support for HTTP Sessions";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.CLIENT;
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/index.html#sessions";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> configuration = generatorContext.getConfiguration();
        configuration.put("micronaut.session.http.cookie", true);
        configuration.put("micronaut.session.http.header", true);
        if (generatorContext.isFeaturePresent(RedisLettuce.class)) {
            configuration.put("micronaut.session.http.redis.enabled", true);
        }
        generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-session")
                .compile());
    }
}
