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
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Implements the {@link CreateOperation} interface for applications.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/create")
public class CreateController extends AbstractCreateController implements CreateOperation {

    /**
     * Default constructor.
     *
     * @param projectGenerator The project generator
     */
    public CreateController(ProjectGenerator projectGenerator) {
        super(projectGenerator);
    }

    @Override
    @Get(uri = "/{type}/{name}{?features,lang,build,test}", produces = "application/zip")
    @ApiResponse(
            content = @Content(
                    mediaType = "application/zip"
            )
    )
    public HttpResponse<Writable> createApp(
            ApplicationType type,
            String name,
            @Nullable List<String> features,
            BuildTool build,
            TestFramework test,
            Language lang) {
        return super.createApp(type, name, features, build, test, lang);
    }
}
