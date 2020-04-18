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
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.command.CreateCommand;
import io.micronaut.starter.feature.validation.FeatureValidator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@link CreateOperation} interface for applications.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/create")
public class CreateAppController extends AbstractCreateController implements CreateOperation {
    public CreateAppController(
            CreateAppCommand.CreateAppFeatures createAppFeatures,
            FeatureValidator featureValidator,
            ContextFactory contextFactory) {
        super(createAppFeatures, featureValidator, contextFactory);
    }

    @Override
    @Get(uri = "/app/{name}{?features}", produces = "application/zip")
    public HttpResponse<Writable> createApp(String name, @Nullable List<String> features) {
        return super.createApp(name, features);
    }

    @Override
    protected CreateCommand buildCommand(@Nullable List<String> features) {
        return new CreateAppCommand(
                (CreateAppCommand.CreateAppFeatures) availableFeatures,
                featureValidator,
                contextFactory

        ) {
            @Override
            protected List<String> getSelectedFeatures() {
                return features != null ? features : new ArrayList<>();
            }
        };
    }
}
