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
package io.micronaut.starter.feature.function;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.template.handlerReadme;

/**
 * Interface to be implemented by features which require the user to define a Handler Class. e.g. {@link io.micronaut.starter.feature.function.awslambda.AwsLambda}
 * @author Sergio del Amo
 */
public interface HandlerClassFeature {

    /**
     *
     * @param feature Feature
     * @param generatorContext Generator Context
     * @param documentationLink A link to documentation
     * @return a Rocker Model
     */
    default RockerModel readmeRockerModel(@NonNull Feature feature,
                                          @NonNull GeneratorContext generatorContext,
                                          @Nullable DocumentationLink documentationLink) {
        return handlerReadme.template(feature,
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                documentationLink);
    }

    /**
     *
     * @param applicationType Type of application
     * @param project Project
     * @return The handler class
     */
    @Nullable
    String handlerClass(@NonNull ApplicationType applicationType, @NonNull Project project);
}
