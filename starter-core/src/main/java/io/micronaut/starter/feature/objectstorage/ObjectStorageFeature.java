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
package io.micronaut.starter.feature.objectstorage;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.naming.NameUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

/**
 * Base class for Object Storage features.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 3.7.0
 */
public interface ObjectStorageFeature extends Feature {

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.CLOUD;
    }

    @Override
    default String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-object-storage/latest/guide/";
    }

    @Override
    @NonNull
    default String getName() {
        return "object-storage-" + NameUtils.hyphenate(getCloudProvider());
    }

    @Override
    default String getTitle() {
        return "Object Storage - " + getCloudProvider();
    }

    @Override
    @NonNull
    default String getDescription() {
        return "Micronaut Object Storage provides a uniform API to create, read and delete objects in the major cloud providers. This feature adds the "
                + getCloudProvider() + " implementation";
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.objectstorage")
                .artifactId("micronaut-object-storage-" + NameUtils.hyphenate(getCloudProvider()))
                .compile());
    }

    /**
     * @return the concrete cloud provider in its natural name (eg: "Oracle Cloud").
     */
    @NonNull
    String getCloudProvider();
}
