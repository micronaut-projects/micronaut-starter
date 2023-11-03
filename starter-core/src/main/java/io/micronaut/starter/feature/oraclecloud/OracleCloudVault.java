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
package io.micronaut.starter.feature.oraclecloud;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class OracleCloudVault implements DistributedConfigFeature {

    @NonNull
    @Override
    public String getName() {
        return "oracle-cloud-vault";
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud Vault Distributed Configuration";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Oracle Cloud Vault";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#vault";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.oracle.com/en-us/iaas/Content/KeyManagement/home.htm";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.oracleCloudDependency()
                .artifactId("micronaut-oraclecloud-vault")
                .compile());
        generatorContext.getConfiguration().put("oci.config.profile", "DEFAULT");

        Map<String, Object> bootstrapConfiguration = populateBootstrapForDistributedConfiguration(generatorContext);
        bootstrapConfiguration.put("oci.vault.config.enabled", true);
        Map<String, String> map = new HashMap<>();
        map.put("ocid", "");
        map.put("compartment-ocid", "");
        bootstrapConfiguration.put("oci.vault.vaults", Collections.singletonList(map));
    }
}
