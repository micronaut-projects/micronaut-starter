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
package io.micronaut.starter.feature.asciidoctor;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.asciidoctor.template.asciidocGradle;
import io.micronaut.starter.feature.asciidoctor.template.indexAdoc;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class Asciidoctor implements Feature {

    @Override
    public String getName() {
        return "asciidoctor";
    }

    @Override
    public String getDescription() {
        return "Adds Asciidoctor documentation";
    }

    @Override
    public void apply(CommandContext commandContext) {

        if (commandContext.getBuildTool() == BuildTool.gradle) {
            commandContext.addTemplate("asciidocGradle", new RockerTemplate("gradle/asciidoc.gradle", asciidocGradle.template()));
        } else if (commandContext.getBuildTool() == BuildTool.maven) {
            commandContext.getBuildProperties().put("asciidoctor.maven.plugin.version", "2.0.0-RC.1");
            commandContext.getBuildProperties().put("asciidoctorj.version", "2.2.0");
            commandContext.getBuildProperties().put("asciidoctorj.diagram.version", "2.0.1");
            commandContext.getBuildProperties().put("jruby.version", "9.2.11.1");
        }

        commandContext.addTemplate("indexAdoc", new RockerTemplate("src/docs/asciidoc/index.adoc", indexAdoc.template()));


    }
}
