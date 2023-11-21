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
package io.micronaut.starter.options;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Map;
import java.util.TreeMap;

/**
 * JDK versions.
 * <a href="https://www.java.com/releases/">Releases</a>
 * @author graemerocher
 * @since 1.0.0
 */
@Serdeable
public final class JdkVersion {

    private static final Map<Integer, JdkVersion> INSTANCES = new TreeMap<>();

    public static final JdkVersion JDK_8 = new JdkVersion(8);
    public static final JdkVersion JDK_9 = new JdkVersion(9);
    public static final JdkVersion JDK_10 = new JdkVersion(10);
    public static final JdkVersion JDK_11 = new JdkVersion(11);
    public static final JdkVersion JDK_12 = new JdkVersion(12);
    public static final JdkVersion JDK_13 = new JdkVersion(13);
    public static final JdkVersion JDK_14 = new JdkVersion(14);
    public static final JdkVersion JDK_15 = new JdkVersion(15);
    public static final JdkVersion JDK_16 = new JdkVersion(16);
    public static final JdkVersion JDK_17 = new JdkVersion(17);
    public static final JdkVersion JDK_18 = new JdkVersion(18);
    public static final JdkVersion JDK_19 = new JdkVersion(19);
    public static final JdkVersion JDK_20 = new JdkVersion(20);
    public static final JdkVersion JDK_21 = new JdkVersion(21);

    private final int majorVersion;

    private JdkVersion(int majorVersion) {
        if (INSTANCES.containsKey(majorVersion)) {
            throw new IllegalArgumentException("version " + majorVersion + " already registered");
        }

        this.majorVersion = majorVersion;
        INSTANCES.put(majorVersion, this);
    }

    /**
     * Add a new supported version.
     *
     * @param majorVersion the major version
     * @return the new instance
     */
    public static JdkVersion registerNewVersion(int majorVersion) {
        return new JdkVersion(majorVersion);
    }

    /**
     * @return the name
     */
    public String name() {
        return "JDK_" + majorVersion;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof JdkVersion other && other.majorVersion == majorVersion;
    }

    @Override
    public int hashCode() {
        return majorVersion;
    }

    public static JdkVersion valueOf(int majorVersion) {
        return INSTANCES.values().stream()
                .filter(jdkVersion -> jdkVersion.majorVersion == majorVersion)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported JDK version: " + majorVersion + ". Supported values are " + INSTANCES.keySet()));
    }

    public int majorVersion() {
        return majorVersion;
    }

    public boolean greaterThanEqual(@NonNull JdkVersion jdk) {
        return majorVersion >= jdk.majorVersion;
    }
}
