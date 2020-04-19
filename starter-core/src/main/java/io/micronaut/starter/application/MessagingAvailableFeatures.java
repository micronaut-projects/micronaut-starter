package io.micronaut.starter.application;

import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class MessagingAvailableFeatures extends AvailableFeatures {

    public MessagingAvailableFeatures(List<Feature> features) {
        super(features, ApplicationType.MESSAGING);
    }
}