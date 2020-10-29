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
package io.micronaut.starter.feature.other;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.feature.other.template.dockerfile;

import javax.inject.Singleton;
import java.util.Set;

// TODO: Delete Maven plugin is upgraded
@Singleton
public class Docker implements DefaultFeature {

    @Override
    public String getName() {
        return "docker";
    }

    @Override
    public String getTitle() {
        return "Docker Support";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.FUNCTION;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOW.getOrder();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        String jarFile;
        if (generatorContext.getBuildTool().isGradle()) {
            jarFile = "build/libs/" + generatorContext.getProject().getName() + "-*-all.jar";
        } else {
            jarFile = "target/" + generatorContext.getProject().getName() + "-*.jar";
        }
        generatorContext.addTemplate("dockerfile", new RockerTemplate("Dockerfile", dockerfile.template(generatorContext.getProject(), jarFile)));
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return supports(applicationType) && options.getBuildTool() == BuildTool.MAVEN;
    }
}
