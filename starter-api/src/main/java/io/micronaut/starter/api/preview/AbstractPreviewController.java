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
package io.micronaut.starter.api.preview;

import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.Project;
import io.micronaut.starter.api.create.AbstractCreateController;
import io.micronaut.starter.command.CreateCommand;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.MapOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract preview implementation.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractPreviewController extends AbstractCreateController implements PreviewOperation {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCreateController.class);

    /**
     * Abstract implementation of {@link PreviewOperation}.
     *
     * @param availableFeatures The available features
     * @param featureValidator  The feature validator
     * @param contextFactory    The context factory
     */
    protected AbstractPreviewController(AvailableFeatures availableFeatures, FeatureValidator featureValidator, ContextFactory contextFactory) {
        super(availableFeatures, featureValidator, contextFactory);
    }

    @Override
    public Map<String, String> previewApp(String name, @Nullable List<String> features, @Nullable BuildTool buildTool, @Nullable TestFramework testFramework, @Nullable Language lang) throws IOException {
        try {
            Project project = NameUtils.parse(name);
            CreateCommand createAppCommand = buildCommand(lang, buildTool, testFramework, features != null ? features : Collections.emptyList());
            MapOutputHandler outputHandler = new MapOutputHandler();
            createAppCommand.generate(project, outputHandler);
            return outputHandler.getProject();
        } catch (Exception e) {
            LOG.error("Error generating application: " + e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }
    }
}
