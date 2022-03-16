/*
 * Copyright 2017-2020 original authors
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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import jakarta.inject.Singleton;

@Singleton
public class OracleCloudSdk implements Feature {

    public static final String ORACLE_CLOUD_GROUP = "io.micronaut.oraclecloud";

    @Override
    public String getName() {
        return "oracle-cloud-sdk";
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud SDK";
    }

    @Override
    public String getDescription() {
        return "Provides integration with the Oracle Cloud SDK";
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.cloud.oracle.com/en-us/iaas/Content/API/SDKDocs/javasdk.htm";
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(ORACLE_CLOUD_GROUP)
                .artifactId("micronaut-oraclecloud-sdk")
                .compile());
        generatorContext.getConfiguration().put("oci.config.profile", "DEFAULT");
    }
}
