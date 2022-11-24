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
package io.micronaut.starter.cli.command;

import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.template.TemplateRenderer;
import io.micronaut.starter.util.NameUtils;
import jakarta.inject.Inject;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class CodeGenCommand extends BaseCommand implements Callable<Integer> {

    protected final CodeGenConfig config;

    @ReflectiveAccess
    @Option(names = {"-f", "--force"}, description = "Whether to overwrite existing files")
    protected boolean overwrite;

    protected final ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier;
    protected final ConsoleOutput consoleOutput;
    protected BeanContext beanContext;

    public CodeGenCommand(CodeGenConfig config) {
        this.config = config;
        this.outputHandlerSupplier = () -> new FileSystemOutputHandler(FileSystemOutputHandler.getDefaultBaseDirectory(), this);
        this.consoleOutput = null;
    }

    public CodeGenCommand(CodeGenConfig config,
                          ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier,
                          ConsoleOutput consoleOutput) {
        this.config = config;
        this.outputHandlerSupplier = outputHandlerSupplier;
        this.consoleOutput = consoleOutput;
    }

    @Inject
    public void setBeanContext(BeanContext beanContext) {
        this.beanContext = beanContext;
    }

    public abstract boolean applies();

    protected Project getProject(String name) {
        if (name.indexOf('-') > -1) {
            name = NameUtils.getNameFromScript(name);
        }
        if (config != null && config.getDefaultPackage() != null && name.indexOf('.') == -1) {
            return NameUtils.parse(config.getDefaultPackage() + "." + name);
        } else {
            return NameUtils.parse(name);
        }
    }

    protected TemplateRenderer getTemplateRenderer(Project project) throws IOException {
        return TemplateRenderer.create(project, outputHandlerSupplier.get());
    }

    protected TemplateRenderer getTemplateRenderer() throws IOException {
        return TemplateRenderer.create(outputHandlerSupplier.get());
    }

    protected <T extends CodeGenCommand> T getCommand(Class<T> clazz) {
        T bean = beanContext.createBean(clazz, config);
        bean.overwrite = overwrite;
        bean.spec = spec;
        bean.commonOptions = commonOptions;
        return bean;
    }

    @Override
    public void out(String message) {
        if (consoleOutput == null) {
            super.out(message);
        } else {
            consoleOutput.out(message);
        }
    }

    @Override
    public void err(String message) {
        if (consoleOutput == null) {
            super.err(message);
        } else {
            consoleOutput.err(message);
        }
    }

    @Override
    public void warning(String message) {
        if (consoleOutput == null) {
            super.warning(message);
        } else {
            consoleOutput.warning(message);
        }
    }
}
