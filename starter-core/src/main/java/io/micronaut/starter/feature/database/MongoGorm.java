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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.ConfiguredFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeatureConfiguration;
import io.micronaut.starter.options.Language;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class MongoGorm extends ConfiguredFeature {

    private final MongoReactive mongoReactive;

    public MongoGorm(@Named("mongogorm") FeatureConfiguration featureConfiguration,
                     MongoReactive mongoReactive) {
        super(featureConfiguration);
        this.mongoReactive = mongoReactive;
    }

    @Override
    public Optional<Language> getRequiredLanguage() {
        return Optional.of(Language.GROOVY);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(MongoReactive.class)) {
            featureContext.addFeature(mongoReactive);
        }
    }
}
