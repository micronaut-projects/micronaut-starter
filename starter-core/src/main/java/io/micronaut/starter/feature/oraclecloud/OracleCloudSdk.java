package io.micronaut.starter.feature.oraclecloud;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class OracleCloudSdk implements Feature {
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
}
