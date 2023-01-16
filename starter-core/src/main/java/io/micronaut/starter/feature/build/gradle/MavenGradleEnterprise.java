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
package io.micronaut.starter.feature.build.gradle;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.build.maven.templates.extensions;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

@Singleton
class MavenGradleEnterprise {

    static final String SLASH = "/";
    static final String MAVEN_FOLDER = ".mvn";
    static final String EXTENSIONS_XML = "extensions.xml";
    static final String GRADLE_ENTERPRISE_XML = "gradle-enterprise.xml";
    static final String DOT = ".";
    static final String ARTIFACT_ID_GRADLE_ENTERPRISE_MAVEN_EXTENSION = "gradle-enterprise-maven-extension";
    static final String ARTIFACT_ID_COMMON_CUSTOM_USER_DATA_MAVEN_EXTENSION = "common-custom-user-data-maven-extension";

    void applyMaven(GeneratorContext generatorContext, GradleEnterpriseConfiguration server) {
        addMavenTemplate(generatorContext, EXTENSIONS_XML, extensionsRockerModel(generatorContext));
        addMavenTemplate(generatorContext, GRADLE_ENTERPRISE_XML, io.micronaut.starter.feature.build.maven.templates.gradleEnterprise.template(server));
    }

    void addMavenTemplate(GeneratorContext generatorContext, String name, RockerModel rockerModel) {
        String templateName = name.contains(DOT) ? name.substring(0, name.indexOf(DOT)) : name;
        String path = String.join(SLASH, MAVEN_FOLDER, name);
        generatorContext.addTemplate(templateName, new RockerTemplate(path, rockerModel));
    }

    private RockerModel extensionsRockerModel(GeneratorContext generatorContext) {
        return extensions.template(
            generatorContext.resolveCoordinate(ARTIFACT_ID_GRADLE_ENTERPRISE_MAVEN_EXTENSION).getVersion(),
            generatorContext.resolveCoordinate(ARTIFACT_ID_COMMON_CUSTOM_USER_DATA_MAVEN_EXTENSION).getVersion());
    }
}
