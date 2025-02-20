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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.other.template.maindocs;
import io.micronaut.starter.feature.other.template.readme;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.DefaultTemplate;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.Writable;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

@Requires(property = "micronaut.starter.feature.readme.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
        List<Writable> helpTemplates = generatorContext.getHelpTemplates();
        boolean anyFeatureHasDocumentationLinks = generatorContext.getFeatures().getFeatures().stream()
                .anyMatch(f -> {
                    String micronautDocumentation = micronautDocumentation(f, generatorContext);
                    String thirdPartyLinkDocumentation = thirdPartyLinkDocumentation(f, generatorContext);
                    return micronautDocumentation != null || thirdPartyLinkDocumentation != null;
                });
        if (!helpTemplates.isEmpty() || anyFeatureHasDocumentationLinks) {
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

                    for (Feature f : generatorContext.getFeatures().getFeatures()) {
                        String micronautDocumentation = micronautDocumentation(f, generatorContext);
                        String thirdPartyLinkDocumentation = thirdPartyLinkDocumentation(f, generatorContext);
                        if (micronautDocumentation != null || thirdPartyLinkDocumentation != null) {
                            Writable writable = new RockerWritable(readme.template(f.getTitle(), f.getName(), micronautDocumentation, thirdPartyLinkDocumentation));
                            writable.write(outputStream);
                            outputStream.write(lineSeparator);
                        }
                    }
                }
            });
        }
    }

    private static String micronautDocumentation(Feature feature, GeneratorContext generatorContext) {
        String documentation = feature.getMicronautDocumentation(generatorContext);
        if (StringUtils.isNotEmpty(documentation)) {
            return documentation;
        }
        return feature.getMicronautDocumentation();
    }

    private static String thirdPartyLinkDocumentation(Feature feature, GeneratorContext generatorContext) {
        String documentation = feature.getThirdPartyDocumentation(generatorContext);
        if (StringUtils.isNotEmpty(documentation)) {
            return documentation;
        }
        return feature.getThirdPartyDocumentation();
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
