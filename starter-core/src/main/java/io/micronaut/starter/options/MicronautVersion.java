/*
 * Copyright 2017-2023 original authors
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

/**
 * Enum for Micronaut Framework major versions.
 */
public enum MicronautVersion {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int major;

    MicronautVersion(int major) {
        this.major = major;
    }

    public int getMajor() {
        return major;
    }

    @Override
    public String toString() {
        return "" + major;
    }
}
