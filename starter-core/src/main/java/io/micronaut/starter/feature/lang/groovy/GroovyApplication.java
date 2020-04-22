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
package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class GroovyApplication implements GroovyApplicationFeature {

    @Override
    public String mainClassName(Project project) {
        return project.getPackageName() + ".Application";
    }

    @Override
    public String getName() {
        return "application";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI && applicationType != ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        GroovyApplicationFeature.super.apply(generatorContext);

        generatorContext.addTemplate("application", new RockerTemplate("src/main/groovy/{packagePath}/Application.groovy",
                application.template(generatorContext.getProject(), generatorContext.getFeatures())));
    }

    protected String getPath() {
        return "src/main/groovy/{packagePath}/Application.groovy";
    }
}
