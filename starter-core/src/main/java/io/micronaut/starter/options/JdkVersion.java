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

/**
 * JDK versions.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public enum JdkVersion {
    JDK_8(8),
    JDK_11(11),
    JDK_14(14);

    private final int majorVersion;

    JdkVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public static JdkVersion valueOf(int majorVersion) {
        switch (majorVersion) {
            case 8:
                return JDK_8;
            case 11:
                return JDK_11;
            case 14:
                return JDK_14;
            default:
                throw new IllegalArgumentException("Unsupported JDK version: " + majorVersion);
        }
    }

    public int majorVersion() {
        return majorVersion;
    }
}
