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
package io.micronaut.starter.api.diff;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.diff.FeatureDiffer;
import io.micronaut.starter.options.*;
import io.micronaut.starter.util.NameUtils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;

/**
 * A controller for performing Diffs.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Controller("/diff")
public class DiffController implements DiffOperations {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final ProjectGenerator projectGenerator;
    private final FeatureDiffer featureDiffer;
    private final Project project;

    /**
     * Default constructor.
     * @param projectGenerator The project generator
     * @param featureDiffer The feature differ
     */
    public DiffController(ProjectGenerator projectGenerator, FeatureDiffer featureDiffer) {
        this.projectGenerator = projectGenerator;
        this.featureDiffer = featureDiffer;
        this.project = NameUtils.parse("example");
    }

    /**
     * Returns a diff for the given application type and feature.
     *
     * @param type The application type
     * @param feature The feature
     * @param build The build tool
     * @param test The test framework
     * @param lang The lang
     * @param javaVersion The java version
     * @param requestInfo The request info
     * @return A string representing the difference
     */
    @Get(uri = "/{type}/feature/{feature}{?lang,build,test,javaVersion,name}",
            produces = MediaType.TEXT_PLAIN)
    @Override
    @ApiResponse(responseCode = "404", description = "If no difference is found")
    @ApiResponse(responseCode = "400", description = "If the supplied parameters are invalid")
    @ApiResponse(responseCode = "200", description = "A textual diff", content = @Content(mediaType = "text/plain"))
    public Flowable<String> diffFeature(
            @NotNull ApplicationType type,
            @Nullable String name,
            @NonNull @NotBlank String feature,
            @Nullable BuildTool build,
            @Nullable TestFramework test,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion,
            @Parameter(hidden = true) RequestInfo requestInfo) {

        return Flowable.create(emitter -> {
            try {
                Project project = name != null ? NameUtils.parse(name) : this.project;
                Options options = new Options(
                        lang != null ? lang : Language.JAVA,
                        test != null ? test : TestFramework.JUNIT,
                        build != null ? build : BuildTool.GRADLE
                );
                ProjectGenerator projectGenerator = this.projectGenerator;
                featureDiffer.produceDiff(
                        projectGenerator,
                        project,
                        type,
                        options,
                        Collections.singletonList(feature),
                        s -> emitter.onNext(s + LINE_SEPARATOR)
                );
                emitter.onComplete();
            } catch (IllegalArgumentException e) {
                emitter.onError(new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
            } catch (Exception e) {
                emitter.onError(new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not produce diff: " + e.getMessage()));
            }
        }, BackpressureStrategy.BUFFER);


    }
}
