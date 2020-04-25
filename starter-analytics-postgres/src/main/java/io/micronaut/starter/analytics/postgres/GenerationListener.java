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
package io.micronaut.starter.analytics.postgres;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.core.util.StringUtils;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.starter.api.StarterConfiguration;
import io.micronaut.starter.api.event.ApplicationGeneratingEvent;
import io.micronaut.starter.application.generator.GeneratorContext;
import org.postgresql.PGProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static io.micronaut.starter.analytics.postgres.GenerationListener.ENABLED;

/**
 * A listener that will listen for {@link ApplicationGeneratingEvent} and save the generation.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Singleton
@TypeHint(
    typeNames = "org.postgresql.Driver",
    value = PGProperty.class,
    accessType = {
            TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS,
            TypeHint.AccessType.ALL_DECLARED_FIELDS
    }
)
@Requires(env = Environment.GOOGLE_COMPUTE)
@Requires(property = ENABLED, defaultValue = StringUtils.FALSE)
public class GenerationListener {
    public static final String ENABLED = StarterConfiguration.PREFIX + ".analytics.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(GenerationListener.class);
    private final ExecutorService executorService;
    private final ApplicationRepository applicationRepository;
    private final FeatureRepository featureRepository;

    /**
     * Default constructor.
     * @param executorService The executor service
     * @param applicationRepository The application repository
     * @param featureRepository The feature repository
     */
    public GenerationListener(
            @Named(TaskExecutors.IO)
            ExecutorService executorService,
            ApplicationRepository applicationRepository,
            FeatureRepository featureRepository) {
        this.executorService = executorService;
        this.applicationRepository = applicationRepository;
        this.featureRepository = featureRepository;
    }

    @EventListener
    void onApplicationGenerated(ApplicationGeneratingEvent event) {
        // run the operation in the background so that we don't block
        // the event loop or slow down project generation
        executorService.execute(() -> {
            try {
                save(event.getSource());
            } catch (Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Error occurred saving application generated event: " + e.getMessage(), e);
                }
            }
        });
    }

    @Transactional
    void save(GeneratorContext context) {
        Application application = applicationRepository.save(new Application(
                context.getApplicationType(),
                context.getLanguage(),
                context.getBuildTool(),
                context.getTestFramework(),
                context.getJdkVersion()
        ));
        List<Feature> features = context.getFeatures().stream()
                .map(feature -> new Feature(application, feature)).collect(Collectors.toList());
        featureRepository.saveAll(features);
    }

}
