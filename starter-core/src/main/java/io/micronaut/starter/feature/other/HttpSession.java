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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.redis.RedisLettuce;
import jakarta.inject.Singleton;

import java.util.Map;

@Requires(property = "micronaut.starter.feature.http.session.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
    public String getCategory() {
        return Category.CLIENT;
    }

    @Nullable
    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
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
        generatorContext.addDependency(MicronautDependencyUtils.sessionDependency()
                .artifactId("micronaut-session")
                .compile());
    }
}
