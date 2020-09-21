package io.micronaut.starter.feature.aws;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class AwsV2Sdk implements Feature {
    @Override
    public String getName() {
        return "aws-v2-sdk";
    }

    @Override
    public String getTitle() {
        return "AWS v2 SDK";
    }

    @Override
    public String getDescription() {
        return "Provides integration with the AWS v2 SDK";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/welcome.html";
    }
}
