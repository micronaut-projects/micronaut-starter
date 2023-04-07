/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.feature.graalvm;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.aws.Cdk;
import io.micronaut.starter.feature.github.workflows.docker.GraalVMDockerRegistryWorkflow;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class GraalVMFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof GraalVM || f instanceof GraalVMDockerRegistryWorkflow)) {
            if (!supports(options.getLanguage())) {
                throw new IllegalArgumentException("GraalVM is not supported in " + StringUtils.capitalize(options.getLanguage().getName()) + " applications");
            }

            if (options.getJavaVersion().majorVersion() > JdkVersion.JDK_17.majorVersion()) {
                throw new IllegalArgumentException("GraalVM with native image only supports up to JDK 17");
            }

            // See https://github.com/micronaut-projects/micronaut-maven-plugin/issues/373
            if (options.getBuildTool() == BuildTool.MAVEN && features.stream().anyMatch(Cdk.class::isInstance)) {
                throw new IllegalArgumentException("Maven, CDK and GraalVM are not yet supporteds");
            }
        }
    }

    public boolean supports(Language language) {
        return language != Language.GROOVY;
    }
}
