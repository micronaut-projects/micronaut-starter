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
package io.micronaut.starter.api.create;

import io.micronaut.context.BeanLocator;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.starter.Project;
import io.micronaut.starter.api.ApplicationTypes;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.command.CreateCliCommand;
import io.micronaut.starter.command.CreateCommand;
import io.micronaut.starter.command.CreateGrpcCommand;
import io.micronaut.starter.io.ZipOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Abstract implementation of a create controller.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractCreateController implements CreateOperation {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCreateController.class);
    private final BeanLocator beanLocator;

    /**
     * Abstract implementation of {@link CreateOperation}.
     * @param beanLocator The bean locator
     */
    protected AbstractCreateController(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Override
    public HttpResponse<Writable> createApp(
            ApplicationTypes type,
            String name,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang) {
        Project project = NameUtils.parse(name);
        MutableHttpResponse<Writable> response = HttpResponse.created(new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                try {
                    CreateCommand createAppCommand = buildCreateCommand(type);
                    configureCommand(createAppCommand, buildTool, lang, testFramework, features);
                    createAppCommand.generate(project, new ZipOutputHandler(outputStream));
                    outputStream.flush();
                } catch (Exception e) {
                    LOG.error("Error generating application: " + e.getMessage(), e);
                    throw new IOException(e.getMessage(), e);
                }
            }

            @Override
            public void writeTo(Writer out) {
                // no-op, output stream used
            }
        });
        return response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + getFilename());
    }

    /**
     * @param type The type
     * @return The command
     */
    protected @Nonnull CreateCommand buildCreateCommand(@Nonnull ApplicationTypes type) {
        switch (type) {
            case cli:
                return beanLocator.getBean(CreateCliCommand.class);
            case grpc:
                return beanLocator.getBean(CreateGrpcCommand.class);
            case app:
            default:
                return beanLocator.getBean(CreateAppCommand.class);
        }
    }

    /**
     * Configures the command for the given arguments.
     * @param createAppCommand The command
     * @param buildTool The build tool
     * @param lang The language
     * @param testFramework The test framework
     * @param features The features
     */
    protected void configureCommand(
            CreateCommand createAppCommand,
            @Nullable BuildTool buildTool,
            @Nullable Language lang,
            @Nullable TestFramework testFramework,
            @Nullable List<String> features) {
        if (buildTool != null) {
            createAppCommand.setBuildTool(buildTool);
        }
        if (lang != null) {
            createAppCommand.setLang(lang);
        }
        if (testFramework != null) {
            createAppCommand.setTestFramework(testFramework);
        }
        if (features != null) {
            createAppCommand.setFeatures(features);
        }
    }

    /**
     * @return The file name to return.
     */
    protected @Nonnull String getFilename() {
        return "application.zip";
    }
}
