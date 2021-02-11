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
package io.micronaut.starter.feature.dekorate;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.VersionInfo;

/**
 * Abstract implementation of Dekorate feature.
 *
 * @author Pavol Gressa
 * @since 2.0
 */
public abstract class AbstractDekorateFeature implements Feature {

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT || applicationType == ApplicationType.GRPC;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().equals(BuildTool.MAVEN)) {
            generatorContext.getBuildProperties().put("dekorate.version", VersionInfo.getBomVersion("dekorate"));
        } else {
            generatorContext.getBuildProperties().put("dekorateVersion", VersionInfo.getBomVersion("dekorate"));
        }
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html";
    }
}
