package io.micronaut.starter.feature.messaging.kafka;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;

import javax.inject.Singleton;

@Singleton
public class KafkaStreams implements Feature {

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

}
