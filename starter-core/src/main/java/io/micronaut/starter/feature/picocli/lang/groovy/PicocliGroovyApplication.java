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
package io.micronaut.starter.feature.picocli.lang.groovy;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.lang.groovy.GroovyApplicationFeature;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;

@Singleton
public class PicocliGroovyApplication implements GroovyApplicationFeature {

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        return project.getPackageName() + "." + project.getClassName() + "Command";
    }

    @Override
    public String getName() {
        return "picocli-groovy-application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.CLI;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        GroovyApplicationFeature.super.apply(generatorContext);

        generatorContext.addTemplate("application", getTemplate(generatorContext.getProject()));
    }

    public RockerTemplate getTemplate(Project project) {
        return new RockerTemplate(getPath(),
                picocliApplication.template(project));
    }

    protected String getPath() {
        return "src/main/groovy/{packagePath}/{className}Command.groovy";
    }
}
