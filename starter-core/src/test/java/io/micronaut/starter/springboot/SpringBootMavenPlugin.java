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
package io.micronaut.starter.springboot;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.build.maven.ParentPom;
import io.micronaut.starter.build.maven.ParentPomFeature;
import io.micronaut.starter.feature.MavenSpecificFeature;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootMavenPlugin implements MavenSpecificFeature, ParentPomFeature, SpringDefaultFeature {

    private final CoordinateResolver coordinateResolver;

    public SpringBootMavenPlugin(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public ParentPom getParentPom() {
        return coordinateResolver.resolve("spring-boot-starter-parent")
                .map(coordinate -> new ParentPom(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion(), true))
                .orElseThrow();
    }

    @Override
    public String getName() {
        return "spring-boot-maven-plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addHelpLink("Official Apache Maven documentation", "https://maven.apache.org/guides/index.html");
        generatorContext.addHelpLink("Spring Boot Maven Plugin Reference", "https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/");
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                        .groupId(SpringBootDependencies.GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
                        .artifactId("spring-boot-maven-plugin")
                .build());
        BuildProperties buildProperties = generatorContext.getBuildProperties();
        buildProperties.put("java.version", "" + generatorContext.getJdkVersion().majorVersion());
    }
}
