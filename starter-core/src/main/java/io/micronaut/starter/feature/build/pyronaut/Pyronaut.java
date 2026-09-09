/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.build.pyronaut;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.MavenCentral;
import io.micronaut.starter.build.MavenLocal;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.build.RepositoryResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.starter.feature.build.BuildFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import io.micronaut.starter.feature.micrometer.CloudWatch;
import io.micronaut.starter.feature.micrometer.Core;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.opentelemetry.OpenTelemetryFeature;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.starter.feature.security.SecurityOAuth2;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.StringTemplate;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Singleton
public class Pyronaut implements BuildFeature {
    private final RepositoryResolver repositoryResolver;

    public Pyronaut(RepositoryResolver repositoryResolver) {
        this.repositoryResolver = repositoryResolver;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getLanguage() == Language.PYTHON;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Collection<Dependency> dependencies = generatorContext.removeDuplicates(
                generatorContext.getDependencies(),
                generatorContext.getLanguage(),
                generatorContext.getBuildTool());
        List<String> additionalModules = testResourcesAdditionalModules(generatorContext);

        PyronautBuild pyronautBuild = new PyronautBuild(generatorContext.getProject().getName(),
                "1.0.0",
                repositories(generatorContext),
                dependencies,
                generatorContext.hasFeature(TestResources.class),
                additionalModules,
                validationSuppressions(generatorContext));
        generatorContext.addTemplate("pyproject", new StringTemplate("pyproject.toml", pyronautBuild.render()));
    }

    @Override
    public @NonNull String getName() {
        return "pyronaut";
    }

    private static List<String> testResourcesAdditionalModules(GeneratorContext generatorContext) {
        if (!generatorContext.hasFeature(TestResources.class)) {
            return List.of();
        }
        return generatorContext.getFeatures().getFeatures()
                .stream()
                .filter(TestResourcesAdditionalModulesProvider.class::isInstance)
                .map(TestResourcesAdditionalModulesProvider.class::cast)
                .flatMap(feature -> feature.getTestResourcesAdditionalModules(generatorContext).stream())
                .distinct()
                .sorted()
                .toList();
    }

    private static List<String> validationSuppressions(GeneratorContext generatorContext) {
        List<String> suppressions = new ArrayList<>();
        if (generatorContext.hasFeature(TestResources.class)) {
            suppressions.add("micronaut.test-resources*");
        }
        if (generatorContext.hasFeature(TestResources.class)
                && generatorContext.hasFeature(R2dbcFeature.class)
                && generatorContext.hasFeature(MigrationFeature.class)) {
            suppressions.add("datasources.*.dialect");
        }
        if (generatorContext.hasFeature(TestResources.class) && generatorContext.hasFeature(R2dbcFeature.class)) {
            suppressions.add("r2dbc.datasources.*");
        }
        if (generatorContext.hasFeature(Management.class)) {
            suppressions.add("endpoints.*.enabled");
            suppressions.add("endpoints.*.sensitive");
        }
        if (generatorContext.hasFeature(Core.class)) {
            suppressions.add("micronaut.metrics.enabled");
            suppressions.add("micronaut.metrics.binders.*");
        }
        if (generatorContext.getFeatures().hasFeature(AwsCloudFeature.class) || generatorContext.hasFeature(CloudWatch.class)) {
            suppressions.add("aws.*");
        }
        if (generatorContext.hasFeature(OpenTelemetryFeature.class)) {
            suppressions.add("otel.*");
        }
        if (generatorContext.hasFeature(SecurityOAuth2.class)) {
            suppressions.add("micronaut.security.oauth2.clients.*.token.auth-method");
        }
        return suppressions;
    }

    private List<String> repositories(GeneratorContext generatorContext) {
        return repositoryResolver.resolveRepositories(generatorContext)
                .stream()
                .map(Pyronaut::repository)
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .toList();
    }

    private static String repository(Repository repository) {
        if (repository instanceof MavenLocal) {
            return null;
        }
        if (repository instanceof MavenCentral) {
            return "mavenCentral";
        }
        return repository.getUrl();
    }
}
