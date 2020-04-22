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
package io.micronaut.starter.feature.picocli;

import io.micronaut.starter.options.Options;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.VersionInfo;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

@Singleton
public class Picocli implements DefaultFeature {

    @Override
    public String getName() {
        return "picocli";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.CLI;
    }

    @Override
    public String getTitle() {
        return "PicoCLI";
    }

    @Override
    public String getDescription() {
        return "Support for creating PicoCLI applications";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            Map.Entry<String, String> entry = VersionInfo.getDependencyVersion("micronaut.picocli");
            generatorContext.getBuildProperties().put(
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }
}
