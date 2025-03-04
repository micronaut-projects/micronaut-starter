package io.micronaut.starter.util;

import io.micronaut.projectgen.core.feature.Features;
import io.micronaut.projectgen.core.options.JdkVersion;

public final class FeaturesUtils {
    private FeaturesUtils() {
    }

    public static String getTargetJdk(Features features, int max) {
        return VersionInfo.toJdkVersion(Math.min(features.javaVersion().majorVersion(), max));
    }

    public static String getTargetJdk(Features features) {
        if (features.javaVersion() == null) {
            return VersionInfo.toJdkVersion(JdkVersion.JDK_17.majorVersion());
        }
        return VersionInfo.toJdkVersion(features.javaVersion().majorVersion());
    }
}
