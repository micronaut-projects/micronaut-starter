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
package io.micronaut.starter.cli.feature.messaging;

import io.micronaut.starter.feature.messaging.Platform;
import picocli.CommandLine;

public class PlatformConverter implements CommandLine.ITypeConverter<Platform> {

    public static final Platform DEFAULT_PLATFORM = Platform.KAFKA;

    @Override
    public Platform convert(String value) throws Exception {
        if (value == null) {
            return DEFAULT_PLATFORM;
        }
        for (Platform p: Platform.values()) {
            if (value.equalsIgnoreCase(p.name())) {
                return p;
            }
        }
        return DEFAULT_PLATFORM;
    }
}
