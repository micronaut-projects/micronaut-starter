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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.starter.feature.other.template.maindocs;
import io.micronaut.starter.feature.other.template.readme;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.template.DefaultTemplate;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.Writable;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Requires(property = "micronaut.starter.feature.readme.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Readme implements DefaultFeature {

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @NonNull
    @Override
    public String getName() {
        return "readme";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        List<Feature> featuresWithDocumentationLinks = generatorContext.getFeatures().getFeatures().stream().filter(feature -> feature.getFrameworkDocumentation(generatorContext) != null || feature.getThirdPartyDocumentation(generatorContext) != null).collect(Collectors.toList());
        List<Writable> helpTemplates = generatorContext.getHelpTemplates();
        if (!helpTemplates.isEmpty() || !featuresWithDocumentationLinks.isEmpty()) {
            generatorContext.addTemplate("readme", new DefaultTemplate(Template.ROOT, "README.md") {
                @Override
                public void write(OutputStream outputStream) throws IOException {
                    Writable mainDocsWritable = new RockerWritable(maindocs.template());
                    mainDocsWritable.write(outputStream);

                    byte[] lineSeparator = System.lineSeparator().getBytes(Charset.defaultCharset());
                    for (Writable writable : generatorContext.getHelpTemplates()) {
                        writable.write(outputStream);
                        outputStream.write(lineSeparator);
                    }

                    for (Feature feature : featuresWithDocumentationLinks) {
                        Writable writable = new RockerWritable(readme.template(feature));
                        writable.write(outputStream);
                        outputStream.write(lineSeparator);
                    }
                }
            });
        }
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
