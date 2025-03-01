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
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.aws.Cdk;
import io.micronaut.starter.feature.github.workflows.docker.GraalVMDockerRegistryWorkflow;
import io.micronaut.projectgen.core.feature.FeatureValidator;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Singleton
public class GraalVMFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, Set<Feature> features) {

    }

    @Override
    public void validatePostProcessing(Options options, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof GraalVM || f instanceof GraalVMDockerRegistryWorkflow)) {
            if (!supports(options.language())) {
                throw new IllegalArgumentException("GraalVM is not supported in " + StringUtils.capitalize(options.language().getName()) + " applications");
            }

            if (options.javaVersion().majorVersion() > JdkVersion.JDK_21.majorVersion()) {
                throw new IllegalArgumentException("GraalVM with native image only supports up to JDK 21");
            }

            // See https://github.com/micronaut-projects/micronaut-maven-plugin/issues/373
            if (OptionUtils.hasMavenBuildTool(options) && features.stream().anyMatch(Cdk.class::isInstance)) {
                throw new IllegalArgumentException("Maven, CDK and GraalVM are not yet supported");
            }
        }
    }

    public static List<Language> supportedLanguages() {
        return Stream.of(Language.values())
                .filter(GraalVMFeatureValidator::supports)
                .toList();
    }

    public static boolean supports(Language language) {
        return language != Language.GROOVY;
    }
}
