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
package io.micronaut.starter.api.event;

import io.micronaut.context.event.ApplicationEvent;
import io.micronaut.starter.application.generator.GeneratorContext;

/**
 * An event fired when an application is generated.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public class ApplicationGeneratingEvent extends ApplicationEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param generatorContext The generator context.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationGeneratingEvent(GeneratorContext generatorContext) {
        super(generatorContext);
    }

    @Override
    public GeneratorContext getSource() {
        return (GeneratorContext) super.getSource();
    }
}
