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
package io.micronaut.starter.feature.micrometer;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.function.oraclefunction.OracleCloudFeature;
import io.micronaut.starter.feature.other.Management;
import jakarta.inject.Singleton;

@Singleton
public class OracleCloud extends MicrometerFeature implements OracleCloudFeature, MicrometerRegistryFeature  {

    public static final String ARTIFACT_ID_MICRONAUT_ORACLECLOUD_MICROMETER = "micronaut-oraclecloud-micrometer";
    public static final Dependency DEPENDENCY_MICRONAUT_ORACLE_CLOUD_MICROMETER = MicronautDependencyUtils.oracleCloudDependency()
            .artifactId("micronaut-oraclecloud-bmc-monitoring")
            .compile()
            .build();

    public OracleCloud(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getDescription() {
        return "Adds support for Micrometer metrics (w/ Oracle Cloud reporter)";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#micrometer";
    }

    @Override
    public void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(micrometerDependency());
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_ORACLE_CLOUD_MICROMETER);
    }

    @Override
    public void addConfiguration(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".oraclecloud.enabled", true);
        generatorContext.getConfiguration().put(EXPORT_PREFIX + ".oraclecloud.namespace", "change-me");
    }

    @Override
    @NonNull
    public Dependency.Builder micrometerDependency() {
        return MicronautDependencyUtils.oracleCloudDependency().artifactId(ARTIFACT_ID_MICRONAUT_ORACLECLOUD_MICROMETER).compile();
    }

    @Override
    public String getImplementationName() {
        return "oracle-cloud";
    }
}
