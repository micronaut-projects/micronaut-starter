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
package io.micronaut.starter.feature.messaging.kafka;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.messaging.MessagingFeature;

import javax.inject.Singleton;

@Singleton
public class KafkaStreams implements MessagingFeature {

    private final Kafka kafka;

    public KafkaStreams(Kafka kafka) {
        this.kafka = kafka;
    }

    @NonNull
    @Override
    public String getName() {
        return "kafka-streams";
    }

    @Override
    public String getTitle() {
        return "Kafka Streams";
    }

    @Override
    public String getDescription() {
        return "Adds support for Kafka Streams";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Kafka.class)) {
            featureContext.addFeature(kafka);
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html#kafkaStream";
    }
}
