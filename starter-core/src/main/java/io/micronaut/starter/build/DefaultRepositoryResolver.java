/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.build;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DefaultRepositoryResolver implements RepositoryResolver {
    private Map<String, Repository> repositories = new HashMap<>();

    public DefaultRepositoryResolver() {
        addRepository(new MavenCentral());
    }

    @Override
    @NonNull
    public List<Repository> resolveRepositories(@NonNull GeneratorContext generatorContext) {
        addFeatureWhichRequireRepositories(generatorContext);
        return new ArrayList<>(repositories.values());
    }

    private void addRepository(Repository repository) {
        repositories.put(repository.getId(), repository);
    }

    private void addFeatureWhichRequireRepositories(GeneratorContext generatorContext) {
        if (addSnapshotRepository(generatorContext)) {
            addRepository(new MicronautSnapshotRepository());
        }
        generatorContext.getFeatures()
                .getFeatures()
                .stream()
                .filter(RequiresRepository.class::isInstance)
                .forEach(f -> {
                    for (Repository repository : ((RequiresRepository) f).getRepositories()) {
                        addRepository(repository);
                    }
                });
    }

    boolean addSnapshotRepository(GeneratorContext generatorContext) {
        return generatorContext.getFramework().equals(Options.FRAMEWORK_MICRONAUT) && VersionInfo.isMicronautSnapshot();
    }
}
