package io.micronaut.starter.util;

import io.micronaut.projectgen.core.feature.Features;

public final class FeaturesUtils {
    private FeaturesUtils() {
    }

    public static String getTargetJdk(Features features, int max) {
        return VersionInfo.toJdkVersion(Math.min(features.javaVersion().majorVersion(), max));
    }

    public static String getTargetJdk(Features features) {
        return VersionInfo.toJdkVersion(features.javaVersion().majorVersion());
    }
}
