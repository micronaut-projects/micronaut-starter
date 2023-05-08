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

import java.util.Arrays;
import java.util.List;

/**
 * JDK versions.
 * <a href="https://www.java.com/releases/">Releases</a>
 * @author graemerocher
 * @since 1.0.0
 */
public enum JdkVersion {
    JDK_8(8, true),
    JDK_9(9, false),
    JDK_10(9, false),
    JDK_11(11, true),
    JDK_12(12, false),
    JDK_13(13, false),
    JDK_14(14, false),
    JDK_15(15, false),
    JDK_16(16, false),
    JDK_17(17, true),
    JDK_19(19, false),
    JDK_20(20, false);

    private static final List<Integer> SUPPORTED_JDKS = Arrays.stream(JdkVersion.values()).map(JdkVersion::majorVersion).toList();

    private final int majorVersion;
    private final boolean lts;

    JdkVersion(int majorVersion, boolean lts) {
        this.majorVersion = majorVersion;
        this.lts = lts;
    }

    public static JdkVersion valueOf(int majorVersion) {
        return Arrays
                .stream(JdkVersion.values())
                .filter(jdkVersion -> jdkVersion.majorVersion == majorVersion)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported JDK version: " + majorVersion + ". Supported values are " + SUPPORTED_JDKS));
    }

    public int majorVersion() {
        return majorVersion;
    }

    public boolean isLts() {
        return lts;
    }

    public boolean greaterThanEqual(JdkVersion jdk) {
        return majorVersion >= jdk.majorVersion;
    }
}
