package io.micronaut.starter.feature.aws;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;
import io.micronaut.starter.feature.aws.template.cdkjson;

@Singleton
public class Cdk implements MultiprojectFeature {
    @Override
    public String getName() {
        return "aws-cdk";
    }

    @Override
    public String getTitle() {
        return "AWS CDK (Cloud Development Kit)";
    }

    @Override
    public String getDescription() {
        return "Adds Amazon Cloud Development Kit support";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT || applicationType == ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("infrackdjson",
                new RockerTemplate( "infra/cdk.json", cdkjson.template(), "infra"));
    }
}
