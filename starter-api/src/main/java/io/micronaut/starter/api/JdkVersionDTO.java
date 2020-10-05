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
 * See the License for the specific jdkVersion governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.api;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.MessageSource;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.naming.Described;
import io.micronaut.core.naming.Named;
import io.micronaut.starter.options.JdkVersion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO objects for {@link JdkVersion}.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Schema(name = "JdkVersionInfo")
@Introspected
public class JdkVersionDTO extends Linkable implements Named, Described, Selectable<String> {
    static final String MESSAGE_PREFIX = StarterConfiguration.PREFIX + ".jdkVersion.";
    private final String name;
    private final String description;
    private final Integer majorVersion;
    private final String value;

    /**
     * @param jdkVersion The jdkVersion
     */
    public JdkVersionDTO(JdkVersion jdkVersion) {
        this.name = jdkVersion.toString();
        this.description = String.valueOf(jdkVersion.majorVersion());
        this.majorVersion = jdkVersion.majorVersion();
        this.value= jdkVersion.name();
    }

    /**
     * @param name the name
     * @param description The description
     */
    @Creator
    @Internal
    JdkVersionDTO(String name, String description, Integer majorVersion, String value) {
        this.name = name;
        this.description = description;
        this.majorVersion = majorVersion;
        this.value= value;
    }

    /**
     * i18n constructor.
     * @param jdkVersion The type
     * @param messageSource The message source
     * @param messageContext The message context
     */
    @Internal
    JdkVersionDTO(JdkVersion jdkVersion, MessageSource messageSource, MessageSource.MessageContext messageContext) {
        String name = jdkVersion.name();
        this.name = name;
        this.description = messageSource.getMessage(MESSAGE_PREFIX + name + ".description", messageContext, name);
        this.majorVersion = jdkVersion.majorVersion();
        this.value= jdkVersion.name();
    }


    @Override
    @Schema(description = "A description of the jdkVersion")
    public String getDescription() {
        return description;
    }

    @Override
    @Schema(description = "The name of the jdkVersion")
    @NonNull
    public String getName() {
        return name;
    }

    @Override
    @Schema(description = "The value of the jdkVersion for select options")
    @NonNull
    public String getValue() {
        return value;
    }

    @Override
    @Schema(description = "The label of the jdkVersion for select options")
    public String getLabel() {
        return description;
    }
}
