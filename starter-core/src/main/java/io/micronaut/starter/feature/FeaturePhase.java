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
package io.micronaut.starter.feature;

public enum FeaturePhase {

    LOWEST(-200),
    LOW(-100),
    DEFAULT(0),
    LANGUAGE(300),
    TEST(400),
    BUILD_PLUGIN(400),
    BUILD(600),
    HIGH(700),
    HIGHEST(800);

    private final int order;

    FeaturePhase(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

}
