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
package io.micronaut.starter.analytics.postgres;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.starter.analytics.Generated;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller(AnalyticsController.PATH)
@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AnalyticsController {

    public static final String PATH = "/analytics";

    private final ApplicationRepository applicationRepository;
    private final FeatureRepository featureRepository;
    private final ExcelGenerator excelGenerator;

    public AnalyticsController(
            ApplicationRepository applicationRepository,
            FeatureRepository featureRepository,
            ExcelGenerator excelGenerator) {
        this.applicationRepository = applicationRepository;
        this.featureRepository = featureRepository;
        this.excelGenerator = excelGenerator;
    }

    @Get("/top/features")
    List<TotalDTO> topFeatures() {
        return featureRepository.topFeatures();
    }

    @Get("/top/jdks")
    List<TotalDTO> topJdks() {
        return featureRepository.topJdkVersion();
    }

    @Get("/top/buildTools")
    List<TotalDTO> topBuilds() {
        return featureRepository.topBuildTools();
    }

    @Get("/top/languages")
    List<TotalDTO> topLanguages() {
        return featureRepository.topLanguages();
    }

    @Get("/top/testFrameworks")
    List<TotalDTO> topTestFrameworks() {
        return featureRepository.topTestFrameworks();
    }

    /**
     * Report analytics.
     * @param generated The generated data
     * @return A future
     */
    @Post("/report")
    @Transactional
    HttpStatus applicationGenerated(@NonNull @Body Generated generated) {
        Application application = new Application(
                generated.getType(),
                generated.getLanguage(),
                generated.getBuildTool(),
                generated.getTestFramework(),
                generated.getJdkVersion(),
                generated.getMicronautVersion()
        );
        Application saved = applicationRepository.save(application);
        List<Feature> features = generated.getSelectedFeatures().stream()
                .map(f -> new Feature(saved, f.getName()))
                .collect(Collectors.toList());

        featureRepository.saveAll(features);
        return HttpStatus.ACCEPTED;
    }

    /**
     * Generates an Excel spreadsheet containing all the applications stored in the repository.
     * @return an Excel spreadsheet.
     * @throws IOException if the spreadsheet cannot be written to the buffer.
     */
    @Get("/excel")
    @Produces(MediaType.MICROSOFT_EXCEL_OPEN_XML)
    HttpResponse<StreamedFile> generateExcel() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            excelGenerator.generateExcel().write(out);
            return HttpResponse.ok()
                    .body(new StreamedFile(new ByteArrayInputStream(out.toByteArray()), MediaType.MICROSOFT_EXCEL_OPEN_XML_TYPE)
                    .attach("applications." + MediaType.EXTENSION_XLSX));
        }
    }
}
