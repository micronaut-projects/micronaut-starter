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
import java.util.stream.Collectors;

/**
 * JDK versions.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public enum JdkVersion {
    JDK_8(8),
    JDK_11(11),
    JDK_17(17);

    public static final JdkVersion DEFAULT_OPTION = JDK_11;

    private static final List<Integer> SUPPORTED_JDKS = Arrays.stream(JdkVersion.values()).map(JdkVersion::majorVersion).collect(Collectors.toList());

    private final int majorVersion;

    JdkVersion(int majorVersion) {
        this.majorVersion = majorVersion;
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

}
