/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.aws;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class AwsLambdaFeatureValidator implements FeatureValidator {
    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof AwsLambda) && features.stream().noneMatch(f -> f instanceof GraalVM)) {
            JdkVersion javaVersion = options.getJavaVersion();
            if (!supports(options.getJavaVersion())) {
                throw new IllegalArgumentException(String.format("AWS Lambda does not have a Java %s runtime", javaVersion.majorVersion()));
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }

    public static List<JdkVersion> supportedJdks() {
        return Stream.of(JdkVersion.values())
                .filter(AwsLambdaFeatureValidator::supports)
                .collect(Collectors.toList());
    }

    public static JdkVersion firstSupportedJdk() {
        return Stream.of(JdkVersion.values())
                .filter(AwsLambdaFeatureValidator::supports)
                .findFirst()
                .orElse(JdkVersion.JDK_17);
    }
    
    public static boolean supports(JdkVersion jdkVersion) {
        return jdkVersion == JdkVersion.JDK_8 || jdkVersion == JdkVersion.JDK_11 || jdkVersion == JdkVersion.JDK_17;
    }
}
