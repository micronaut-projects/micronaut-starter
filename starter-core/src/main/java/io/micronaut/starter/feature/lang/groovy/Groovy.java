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
package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.LanguageFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.features.maven.GroovyMavenPlusPlugin;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Requires(property = "micronaut.starter.feature.groovy.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Groovy implements LanguageFeature {
    public static final String GROUP_ID_GROOVY = "org.apache.groovy";
    protected static final Dependency DEPENDENCY_MICRONAUT_GROOVY_RUNTIME = MicronautDependencyUtils.groovyDependency()
            .artifactId("micronaut-runtime-groovy")
            .compile()
            .build();
    protected static final Dependency DEPENDENCY_MICRONAUT_INJECT_GROOVY = MicronautDependencyUtils.coreDependency()
            .artifactId("micronaut-inject-groovy")
            .developmentOnly()
            .build();
    protected static final Dependency DEPENDENCY_GROOVY = new Dependency.Builder()
            .groupId(GROUP_ID_GROOVY)
            .artifactId("groovy")
            .versionProperty("groovy.version")
            .compile()
            .build();
    protected final List<GroovyApplicationFeature> applicationFeatures;

    protected final GroovyMavenPlusPlugin groovyMavenPlusPlugin;

    public Groovy(List<GroovyApplicationFeature> applicationFeatures,
                  GroovyMavenPlusPlugin groovyMavenPlusPlugin) {
        this.applicationFeatures = applicationFeatures;
        this.groovyMavenPlusPlugin = groovyMavenPlusPlugin;
    }

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
            featureContext.addFeature(groovyMavenPlusPlugin);
        }
        processSelectedFeatured(featureContext, feature -> true);
    }

    protected void processSelectedFeatured(FeatureContext featureContext, Predicate<Feature> filter) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            ApplicationType type = featureContext.getOptions() instanceof MicronautOptions mnOptions ? mnOptions.applicationType() : null;
            applicationFeatures.stream()
                    .filter(filter)
                    .filter(f -> f.supports(MicronautOptions.builder().applicationType(type).build()))
                    .findFirst()
                    .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            generatorContext.getBuildProperties().put("groovyVersion", VersionInfo.getDependencyVersion("groovy").getValue());
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_INJECT_GROOVY);
            generatorContext.addDependency(DEPENDENCY_GROOVY);
        }
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_GROOVY_RUNTIME);
    }

    @Override
    public boolean isGroovy() {
        return true;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options.language() == Language.GROOVY;
    }
}
