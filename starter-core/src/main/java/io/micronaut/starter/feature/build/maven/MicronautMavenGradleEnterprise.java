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
package io.micronaut.starter.feature.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.maven.templates.customData;
import io.micronaut.starter.feature.build.maven.templates.extensions;
import io.micronaut.starter.feature.build.maven.templates.gradleEnterprise;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Singleton
public class MicronautMavenGradleEnterprise implements Feature {
    public static final String NAME = "micronaut-maven-gradle-enterprise";
    public static final String SERVER = "https://ge.micronaut.io";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Maven Gradle Enterprise";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (!generatorContext.getBuildTool().isGradle()) {
            generatorContext.addTemplate(
                    "enterprise-extensions",
                    new RockerTemplate(".mvn/extensions.xml",
                    extensions.template(
                            generatorContext.resolveCoordinate("gradle-enterprise-maven-extension").getVersion(),
                            generatorContext.resolveCoordinate("common-custom-user-data-maven-extension").getVersion()
                    ))
            );
            generatorContext.addTemplate(
                    "enterprise-config",
                    new RockerTemplate(".mvn/gradle-enterprise.xml", gradleEnterprise.template(SERVER))
            );
            generatorContext.addTemplate(
                    "custom-enterprise-data",
                    new RockerTemplate(".mvn/gradle-enterprise-custom-user-data.groovy", customData.template())
            );

        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

}
