/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.graalvm;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePredicate;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.template.deploysh;
import io.micronaut.starter.feature.graalvm.template.dockerBuildScript;
import io.micronaut.starter.feature.graalvm.template.dockerfile;
import io.micronaut.starter.feature.graalvm.template.lambdadockerfile;
import io.micronaut.starter.feature.graalvm.template.nativeImageProperties;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class GraalNativeImage implements Feature {

    private final AwsLambdaCustomRuntime awsLambdaCustomRuntime;

    public GraalNativeImage(AwsLambdaCustomRuntime awsLambdaCustomRuntime) {
        this.awsLambdaCustomRuntime = awsLambdaCustomRuntime;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() == Language.GROOVY) {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof GraalNativeImage;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("The GraalVM feature was removed because it does not support Groovy");
                }
            });
        } else {
            if (shouldApplyFeature(featureContext, AwsLambdaCustomRuntime.class)) {
                featureContext.addFeature(awsLambdaCustomRuntime);
            }
        }
    }

    protected boolean shouldApplyFeature(FeatureContext featureContext, Class feature) {
        if (feature == AwsLambdaCustomRuntime.class) {
            if (
                    (
                            featureContext.getApplicationType() == ApplicationType.FUNCTION ||
                                    featureContext.getApplicationType() == ApplicationType.DEFAULT
                    ) &&
                            featureContext.isPresent(AwsLambda.class) &&
                            awsLambdaCustomRuntime.supports(featureContext.getApplicationType()) &&
                            !featureContext.isPresent(feature)
            ) {
                    return true;
            }
        }
        return false;

    }

    @Override
    public String getName() {
        return "graalvm";
    }

    @Override
    public String getTitle() {
        return "GraalVM Native Image";
    }

    @Override
    public String getDescription() {
        return "Allows Building a GraalVM Native Image";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationType applicationType = generatorContext.getApplicationType();
        RockerModel dockerfileRockerModel;
        if (nativeImageWillBeDeployedToAwsLambda(generatorContext)) {
            dockerfileRockerModel = lambdadockerfile.template(generatorContext.getProject(), generatorContext.getBuildTool());
            RockerModel deployshRockerModel = deploysh.template(generatorContext.getProject());
            generatorContext.addTemplate("deploysh", new RockerTemplate("deploy.sh", deployshRockerModel, true));

        } else {
            dockerfileRockerModel = dockerfile.template(generatorContext.getProject(), generatorContext.getBuildTool());
        }
        generatorContext.addTemplate("dockerfile", new RockerTemplate("Dockerfile", dockerfileRockerModel));

        generatorContext.addTemplate("dockerBuildScript", new RockerTemplate("docker-build.sh", dockerBuildScript.template(generatorContext.getProject()), true));

        generatorContext.addTemplate("nativeImageProperties",
                new RockerTemplate("src/main/resources/META-INF/native-image/{packageName}/{name}-application/native-image.properties", nativeImageProperties.template(applicationType, generatorContext.getProject(), generatorContext.getFeatures())
                )
        );
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    protected boolean nativeImageWillBeDeployedToAwsLambda(GeneratorContext generatorContext) {
        return generatorContext.getFeatures().getFeatures().stream().anyMatch(feature -> feature.getName().equals(AwsLambda.FEATURE_NAME_AWS_LAMBDA));
    }

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }
}
