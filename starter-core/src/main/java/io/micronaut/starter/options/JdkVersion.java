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
package io.micronaut.starter.options;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * JDK versions.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public enum JdkVersion {
    JDK_8(8, true),
    JDK_9(9, false),
    JDK_10(10, false),
    JDK_11(11, true),
    JDK_12(12, false),
    JDK_13(13, false),
    JDK_14(14, false),
    JDK_15(15, true);

    private final int majorVersion;
    private final boolean hasSupport;

    JdkVersion(int majorVersion, boolean hasSupport) {
        this.majorVersion = majorVersion;
        this.hasSupport = hasSupport;
    }

    public static JdkVersion valueOf(int majorVersion) {
        switch (majorVersion) {
            case 8:
                return JDK_8;
            case 11:
                return JDK_11;
            case 15:
                return JDK_15;
            default:
                throw new IllegalArgumentException("Unsupported JDK version: " + majorVersion + ". Supported values are " +
                        Arrays.stream(JdkVersion.values()).filter(JdkVersion::hasSupport).map(JdkVersion::majorVersion).collect(Collectors.toList())
                );
        }
    }

    public int majorVersion() {
        return majorVersion;
    }

    public boolean hasSupport() {
        return hasSupport;
    }
}
