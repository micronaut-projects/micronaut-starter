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
package io.micronaut.starter.command;

import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.CodeGenConfig;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.template.TemplateRenderer;
import io.micronaut.starter.util.NameUtils;
import picocli.CommandLine;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class CodeGenCommand extends BaseCommand implements Callable<Integer> {

    protected final CodeGenConfig config;

    @CommandLine.Option(names = {"-f", "--force"}, description = "Whether to overwrite existing files")
    boolean overwrite;

    private final ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier;

    public CodeGenCommand(CodeGenConfig config) {
        this.config = config;
        this.outputHandlerSupplier = () -> new FileSystemOutputHandler(new File(".").getCanonicalFile(), this);
    }

    public abstract boolean applies();

    protected Project getProject(String name, @Nullable CodeGenConfig codeGenConfig) {
        if (name.indexOf('-') > -1) {
            name = NameUtils.getNameFromScript(name);
        }
        if (codeGenConfig != null && codeGenConfig.getDefaultPackage() != null && name.indexOf('.') == -1) {
            return NameUtils.parse(codeGenConfig.getDefaultPackage() + "." + name);
        } else {
            return NameUtils.parse(name);
        }
    }

    protected TemplateRenderer getTemplateRenderer(Project project) throws IOException {
        return TemplateRenderer.create(project, outputHandlerSupplier.get());
    }
}
