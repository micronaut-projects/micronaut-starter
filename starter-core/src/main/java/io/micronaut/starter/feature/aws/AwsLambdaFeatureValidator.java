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

import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.projectgen.core.feature.FeatureValidator;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.starter.options.MicronautJdkVersionConfiguration;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class AwsLambdaFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, Set<Feature> features) {
        if (features.stream().anyMatch(AwsLambda.class::isInstance) && features.stream().noneMatch(GraalVM.class::isInstance)) {
            JdkVersion javaVersion = options.javaVersion();
            if (!supports(options.javaVersion())) {
                throw new IllegalArgumentException("AWS Lambda does not have a Java %s runtime".formatted(javaVersion.majorVersion()));
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, Set<Feature> features) {

    }

    public static List<JdkVersion> supportedJdks() {
        return MicronautJdkVersionConfiguration.SUPPORTED_JDKS.stream()
                .filter(AwsLambdaFeatureValidator::supports)
                .collect(Collectors.toList());
    }

    public static JdkVersion firstSupportedJdk() {
        return MicronautJdkVersionConfiguration.SUPPORTED_JDKS.stream()
                .filter(AwsLambdaFeatureValidator::supports)
                .findFirst()
                .orElse(JdkVersion.JDK_17);
    }
    
    public static boolean supports(JdkVersion jdkVersion) {
        return jdkVersion == JdkVersion.JDK_17 || jdkVersion == JdkVersion.JDK_21;
    }
}
