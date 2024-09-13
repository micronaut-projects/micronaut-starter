/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.oraclecloud;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.oraclefunction.OracleCloudFeature;
import jakarta.inject.Singleton;
import static io.micronaut.starter.feature.Category.LOGGING;

@Singleton
public class OracleCloudLogging implements OracleCloudFeature, Feature {
    public static final String NAME = "oracle-cloud-logging";
    private static final String ARTIFACT_ID_MICRONAUT_ORACLECLOUD_LOGGING = "micronaut-oraclecloud-logging";
    private static final Dependency ORACLE_LOGGING_DEPENDENCY =
            MicronautDependencyUtils.ociDependency()
                    .artifactId(ARTIFACT_ID_MICRONAUT_ORACLECLOUD_LOGGING)
                    .compile()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud Logging";
    }

    @Override
    public String getDescription() {
        return "Provides integration with the Oracle Cloud Logging service";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#logging";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.oracle.com/en-us/iaas/Content/Logging/Concepts/loggingoverview.htm";
    }

    @Override
    public String getCategory() {
        return LOGGING;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        addConfiguration(generatorContext);
    }

    protected void addConfiguration(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("oci.config.profile", "DEFAULT");
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(ORACLE_LOGGING_DEPENDENCY);
    }
}
