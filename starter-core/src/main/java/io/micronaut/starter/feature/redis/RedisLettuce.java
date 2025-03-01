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
package io.micronaut.starter.feature.redis;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;

import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.redis.lettuce.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class RedisLettuce implements Feature {

    @Override
    public String getName() {
        return "redis-lettuce";
    }

    @Override
    public String getTitle() {
        return "Lettuce Redis Driver";
    }

    @Override
    public String getDescription() {
        return "Configures the Lettuce driver for Redis";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("redis.uri", "redis://localhost");
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.redis")
                .artifactId("micronaut-redis-lettuce")
                .compile());
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}

