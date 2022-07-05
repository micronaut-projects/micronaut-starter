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

/**
 * @see <a href="https://github.com/actions/setup-java#supported-distributions">Github Supported Distributions</a>
 *
 * @author Dean Wette
 * @since 3.6.0
 */
public enum JdkDistribution {
    ADOPT("adopt", "Adopt OpenJDK Hotspot"),
    ADOPT_HOTSPOT("adopt-hotspot", "Adopt OpenJDK Hotspot"),
    ADOPT_OPENJ9("adopt-openj9", "Adopt OpenJDK OpenJ9"),
    CORRETTO("corretto", "Amazon Corretto Build of OpenJDK"),
    LIBERICA("liberica", "Liberica JDK"),
    MICROSOFT("microsoft", "Microsoft Build of OpenJDK"),
    TEMURIN("temurin", "Eclipse Temurin"),
    ZULU("zulu", "Zulu OpenJDK");

    public static final JdkDistribution DEFAULT_DISTRIBUTION = TEMURIN;

    private final String distribution;
    private final String description;

    JdkDistribution(String distribution, String description) {
        this.distribution = distribution;
        this.description = description;
    }

    @NonNull
    public String distribution() {
        return distribution;
    }

    @NonNull
    public String description() {
        return description;
    }
}
