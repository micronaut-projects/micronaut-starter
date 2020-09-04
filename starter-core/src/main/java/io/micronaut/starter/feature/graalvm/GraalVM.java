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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;

import javax.inject.Singleton;

@Singleton
public class GraalVM implements Feature {

    private final AwsLambdaCustomRuntime awsLambdaCustomRuntime;

    public GraalVM(AwsLambdaCustomRuntime awsLambdaCustomRuntime) {
        this.awsLambdaCustomRuntime = awsLambdaCustomRuntime;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (shouldApplyFeature(featureContext, AwsLambdaCustomRuntime.class)) {
            featureContext.addFeature(awsLambdaCustomRuntime);
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
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.PACKAGING;
    }
}
