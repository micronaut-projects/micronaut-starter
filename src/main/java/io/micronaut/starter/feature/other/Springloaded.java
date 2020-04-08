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

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePredicate;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.util.VersionInfo;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class Springloaded implements Feature {

    private static final String JAR_NAME = "springloaded-1.2.8.RELEASE.jar";

    @Override
    public String getName() {
        return "springloaded";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        int jdkVersion = VersionInfo.getJavaVersion();
        if (jdkVersion >= 9) {
            Springloaded springloaded = this;
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature == springloaded;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("Springloaded was excluded because it does not support JDK " + jdkVersion);
                }
            });
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        commandContext.addTemplate("springLoadedJar",
                new BinaryTemplate("agent/" + JAR_NAME, classLoader.getResource("springloaded/" + JAR_NAME)));
    }
}
