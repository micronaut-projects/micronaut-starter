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
package io.micronaut.starter.api;

import io.micronaut.core.annotation.NonNull;

import java.util.Locale;

public enum TestFramework {
    JUNIT,
    SPOCK,
    KOTLINTEST,
    KOTEST;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @NonNull
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public io.micronaut.starter.options.TestFramework toTestFramework() {
        switch (this) {
            case SPOCK:
                return io.micronaut.starter.options.TestFramework.SPOCK;
            case KOTLINTEST:
            case KOTEST:
                return io.micronaut.starter.options.TestFramework.KOTEST;
            case JUNIT:
            default:
                return io.micronaut.starter.options.TestFramework.JUNIT;
        }
    }

}
