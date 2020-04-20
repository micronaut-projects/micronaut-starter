package io.micronaut.starter.cli.feature.messaging;

import io.micronaut.starter.feature.messaging.Platform;
import io.micronaut.starter.options.TestFramework;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlatformCandidates extends ArrayList<String> {

    public PlatformCandidates() {
        super(Stream.of(Platform.values()).map(Platform::getName).collect(Collectors.toList()));
    }
}
