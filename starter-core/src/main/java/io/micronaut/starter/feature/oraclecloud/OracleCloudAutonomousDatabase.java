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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class OracleCloudAutonomousDatabase extends DatabaseDriverFeature {

    private static final Dependency.Builder DEPENDENCY_MICRONAUT_ORACLECLOUD_ATP = MicronautDependencyUtils.oracleCloudDependency()
            .artifactId("micronaut-oraclecloud-atp")
            .compile();

    private final OracleCloudSdk oracleCloudSdkFeature;

    public OracleCloudAutonomousDatabase(JdbcFeature jdbcFeature,
                                         TestContainers testContainers,
                                         TestResources testResources,
                                         OracleCloudSdk oracleCloudSdkFeature) {
        super(jdbcFeature, testContainers, testResources);
        this.oracleCloudSdkFeature = oracleCloudSdkFeature;
    }

    @NonNull
    @Override
    public String getName() {
        return "oracle-cloud-atp";
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud Autonomous Transaction Processing (ATP)";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Provides integration with Oracle Cloud Autonomous Database";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#_micronaut_oraclecloud_atp";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.oracle.com/autonomous-database/autonomous-transaction-processing/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public int getOrder() {
        // need to run after the jdbc feature
        return FeaturePhase.DEFAULT.getOrder();
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public boolean embedded() {
        return false;
    }

    @Override
    public String getJdbcUrl() {
        return null;
    }

    @Override
    public String getR2dbcUrl() {
        return null;
    }

    @Override
    public String getDriverClass() {
        return null;
    }

    @Override
    public String getDefaultUser() {
        return "";
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }

    @Override
    public String getDataDialect() {
        return "ORACLE";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(OracleCloudSdk.class)) {
            featureContext.addFeature(oracleCloudSdkFeature);
        }
    }

    @Override
    public Map<String, Object> getAdditionalConfig(GeneratorContext generatorContext) {
        Map<String, Object> config = new LinkedHashMap<>(2);
        config.put("datasources.default.ocid", "");
        config.put("datasources.default.walletPassword", "");
        return config;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_ORACLECLOUD_ATP);
    }
}
