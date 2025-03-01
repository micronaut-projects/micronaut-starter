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
package io.micronaut.starter.build.maven;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.maven.MavenSpecificFeature;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.groovy.maven.plus.plugin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class GroovyMavenPlusPlugin implements MavenSpecificFeature {

    private static final String GROUP_ID_GMAVEN = "org.codehaus.gmavenplus";
    private static final String ARTIFACT_ID_GMAVEN = "gmavenplus-plugin";

    @Override
    public String getName() {
        return "groovy-maven-plus-plugin";
    }

    @Override
    public String getTitle() {
        return "GMavenPlus Maven Plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .groupId(GROUP_ID_GMAVEN)
                    .artifactId(ARTIFACT_ID_GMAVEN)
                    .build());
        }
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://github.com/groovy/GMavenPlus/wiki/Usage";
    }
}
