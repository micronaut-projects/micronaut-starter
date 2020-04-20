package io.micronaut.starter.cli.feature.messaging;

import io.micronaut.starter.feature.messaging.Platform;
import picocli.CommandLine;

public class PlatformConverter implements CommandLine.ITypeConverter<Platform> {

    public static final Platform DEFAULT_PLATFORM = Platform.KAFKA;

    @Override
    public Platform convert(String value) throws Exception {
        if (value == null) {
            return DEFAULT_PLATFORM;
        }
        for (Platform p: Platform.values()) {
            if (value.equalsIgnoreCase(p.name())) {
                return p;
            }
        }
        return DEFAULT_PLATFORM;
    }
}
