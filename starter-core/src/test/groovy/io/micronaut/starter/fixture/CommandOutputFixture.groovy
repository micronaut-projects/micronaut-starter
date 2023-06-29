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
package io.micronaut.starter.fixture

import groovy.transform.CompileStatic
import io.micronaut.context.BeanContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.OperatingSystem
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.MapOutputHandler
import io.micronaut.starter.io.OutputHandler
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.NameUtils

@CompileStatic
trait CommandOutputFixture {
    abstract BeanContext getBeanContext()

    ProjectGenerator getProjectGenerator() {
        beanContext.getBean(ProjectGenerator)
    }

    Map<String, String> generate(ApplicationType type, Options options, List<String> features = []) {
        OutputHandler handler = new MapOutputHandler()
        projectGenerator.generate(type,
                NameUtils.parse("example.micronaut.foo"),
                options,
                OperatingSystem.LINUX,
                features,
                handler,
                ConsoleOutput.NOOP
        )
        handler.getProject()
    }

    Map<String, String> generate(List<String> features = []) {
        generate(ApplicationType.DEFAULT, features)
    }

    Map<String, String> generate(ApplicationType type, List<String> features = []) {
        OutputHandler handler = new MapOutputHandler()
        Options options = new Options()
        projectGenerator.generate(type,
                NameUtils.parse("example.micronaut.foo"),
                options,
                OperatingSystem.LINUX,
                features,
                handler,
                ConsoleOutput.NOOP
        )
        handler.getProject()
    }

    Map<String, String> generate(ApplicationType type, GeneratorContext generatorContext) {
        OutputHandler handler = new MapOutputHandler()
        projectGenerator.generate(type,
                NameUtils.parse("example.micronaut.foo"),
                handler,
                generatorContext)
        handler.getProject()
    }
}
