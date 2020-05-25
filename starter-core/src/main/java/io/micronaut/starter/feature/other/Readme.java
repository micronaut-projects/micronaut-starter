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
package io.micronaut.starter.feature.other;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.other.template.readme;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.Writable;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class Readme implements DefaultFeature {

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "readme";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        List<Feature> featuresWithDocumentationLinks = generatorContext.getFeatures().getFeatures().stream().filter(feature -> feature.getMicronautDocumentation() != null || feature.getThirdPartyDocumentation() != null).collect(Collectors.toList());
        List<Writable> helpTemplates = generatorContext.getHelpTemplates();
        if (!helpTemplates.isEmpty() || !featuresWithDocumentationLinks.isEmpty()) {
            generatorContext.addTemplate("readme", new Template() {
                @Override
                public String getPath() {
                    return "README.md";
                }

                @Override
                public void write(OutputStream outputStream) throws IOException {
                    for (Writable writable : generatorContext.getHelpTemplates()) {
                        writable.write(outputStream);
                    }

                    for (Feature feature : featuresWithDocumentationLinks) {
                        Writable writable = new RockerWritable(readme.template(feature));
                        writable.write(outputStream);
                    }
                }
            });
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

}
