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

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CreateCommand;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.ZipOutputHandler;
import io.micronaut.starter.util.NameUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of a create controller.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractCreateController implements CreateOperation {

    protected final AvailableFeatures availableFeatures;
    protected final FeatureValidator featureValidator;
    protected final ContextFactory contextFactory;

    /**
     * Abstract implementation of {@link CreateOperation}.
     * @param availableFeatures The available features
     * @param featureValidator The feature validator
     * @param contextFactory The context factory
     */
    protected AbstractCreateController(
            AvailableFeatures availableFeatures,
            FeatureValidator featureValidator,
            ContextFactory contextFactory) {
        this.availableFeatures = availableFeatures;
        this.featureValidator = featureValidator;
        this.contextFactory = contextFactory;
    }

    @Override
    public HttpResponse<Writable> createApp(String name, @Nullable List<String> features) {
        Project project = NameUtils.parse(name);
        MutableHttpResponse<Writable> response = HttpResponse.created(new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                CreateCommand createAppCommand = buildCommand(features != null ? features : Collections.emptyList());
                try {
                    createAppCommand.generate(project, new ZipOutputHandler(outputStream));
                    outputStream.flush();
                } catch (Exception e) {
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
     * @return The file name to return.
     */
    protected String getFilename() {
        return "application.zip";
    }

    /**
     * Build the create command.
     * @param features The features
     * @return The command
     */
    protected abstract CreateCommand buildCommand(@Nonnull List<String> features) ;
}
