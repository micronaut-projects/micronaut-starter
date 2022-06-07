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

public enum JdkDistribution {
    ADOPT("adopt", "Adopt OpenJDK Hotspot"),
    CORRETTO("corretto", "Amazon Corretto Build of OpenJDK"),
    GRALLVM("graalvm", "GraalVM"),
    JAVANET("java.net", "Java.net"),
    LIBERICA("liberica", "Liberica JDK"),
    MICROSOFT("microsoft", "Microsoft Build of OpenJDK"),
    ORACLE("oracle", "Oracle"),
    SAPMACHINE("sapmachine", "SapMachine"),
    SEMERU("semeru", "Semeru"),
    TEMURIN("temurin", "Eclipse Temurin"),
    ZULU("zulu", "Zulu OpenJDK");

    public static final JdkDistribution DEFAULT_DISTRIBUTION = TEMURIN;

    private final String distribution;
    private final String description;

    JdkDistribution(String distribution, String description) {
        this.distribution = distribution;
        this.description = description;
    }

    public String distribution() {
        return distribution;
    }

    public String description() {
        return description;
    }
}
