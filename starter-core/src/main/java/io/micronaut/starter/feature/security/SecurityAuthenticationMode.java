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
package io.micronaut.starter.feature.security;

import io.micronaut.core.order.Ordered;

public enum SecurityAuthenticationMode implements Ordered {
    IDTOKEN("idtoken", 1),
    COOKIE("cookie", 2),
    SESSION("session", 3),
    BEARER("bearer", 4);

    private final String value;
    private final int order;

    SecurityAuthenticationMode(String value,
                               int order) {
        this.value = value;
        this.order = order;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
