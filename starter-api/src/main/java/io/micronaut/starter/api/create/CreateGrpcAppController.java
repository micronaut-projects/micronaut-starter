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
import io.micronaut.starter.command.CreateCommand;
import io.micronaut.starter.command.CreateGrpcCommand;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Controller responsible for creating GRPC applications.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/create")
public class CreateGrpcAppController extends AbstractCreateController implements CreateOperation {
    /**
     * Abstract implementation of {@link CreateOperation}.
     *
     * @param availableFeatures The available features
     * @param featureValidator  The feature validator
     * @param contextFactory    The context factory
     */
    protected CreateGrpcAppController(CreateGrpcCommand.CreateGrpcFeatures availableFeatures, FeatureValidator featureValidator, ContextFactory contextFactory) {
        super(availableFeatures, featureValidator, contextFactory);
    }

    @Override
    @Get(uri = "/grpc/{name}{?features,lang,build,test}", produces = "application/zip")
    @ApiResponse(
            content = @Content(
                    mediaType = "application/zip"
            )
    )
    public HttpResponse<Writable> createApp(String name, @Nullable List<String> features, BuildTool build, TestFramework test, Language lang) {
        return super.createApp(name, features, build, test, lang);
    }

    @Override
    protected CreateCommand buildCommand(Language lang, BuildTool buildTool, TestFramework testFramework, @Nullable List<String> features) {
        CreateGrpcCommand.CreateGrpcFeatures availableFeatures = (CreateGrpcCommand.CreateGrpcFeatures) CreateGrpcAppController.this.availableFeatures;
        FeatureValidator featureValidator = CreateGrpcAppController.this.featureValidator;
        ContextFactory contextFactory = CreateGrpcAppController.this.contextFactory;
        return CreateOperation.buildCreateGrpcAppCommand(availableFeatures, featureValidator, contextFactory, features, lang, buildTool, testFramework);
    }

}
