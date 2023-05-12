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
package io.micronaut.starter.api.options;

import io.micronaut.context.MessageSource;
import io.micronaut.context.i18n.ResourceBundleMessageSource;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.starter.api.RequestInfo;
import io.micronaut.starter.api.SelectOptionsDTO;

import io.micronaut.starter.options.JdkVersionConfiguration;
import jakarta.inject.Singleton;

/**
 * Gets Information about select options for the starter.
 *
 * @since 2.2.0
 */
@Controller("/select-options")
public class SelectOptionsController implements SelectOptionsOperations {

    private final MessageSource messageSource;
    private final JdkVersionConfiguration jdkVersionConfiguration;

    public SelectOptionsController(MessageSource messageSource, JdkVersionConfiguration jdkVersionConfiguration) {
        this.messageSource = messageSource;
        this.jdkVersionConfiguration = jdkVersionConfiguration;
    }

    /**
     * Gets select options for the starter
     *
     * @return Select Options and their defaults.
     */
    @Override
    @Get(uri = "/", produces = MediaType.APPLICATION_JSON)
    public SelectOptionsDTO selectOptions(RequestInfo requestInfo) {
        MessageSource.MessageContext context = MessageSource.MessageContext.of(requestInfo.getLocale());
        return SelectOptionsDTO.make(messageSource, context, jdkVersionConfiguration);
    }

    @Singleton
    static class SelectOptionMessages extends ResourceBundleMessageSource {
        SelectOptionMessages() {
            super("select_options");
        }
    }
}
