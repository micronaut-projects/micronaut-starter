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
package io.micronaut.starter.feature.gcp.secretsmanager;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;
import io.micronaut.starter.feature.gcp.GcpFeature;
import jakarta.inject.Singleton;

@Singleton
public class GoogleSecretManager extends GcpFeature implements DistributedConfigFeature {

    @NonNull
    @Override
    public String getName() {
        return "gcp-secrets-manager";
    }

    @Override
    public String getTitle() {
        return "Google Secret Manager";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Google Secrets Manager";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addDependency(gcpSecretManagerDependency());
        populateBootstrapForDistributedConfiguration(generatorContext);
    }

    @NonNull
    private Dependency.Builder gcpSecretManagerDependency() {
        return MicronautDependencyUtils.gcpDependency()
                .artifactId("micronaut-gcp-secret-manager")
                .compile();
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/#secretManager";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://cloud.google.com/secret-manager";
    }

}
