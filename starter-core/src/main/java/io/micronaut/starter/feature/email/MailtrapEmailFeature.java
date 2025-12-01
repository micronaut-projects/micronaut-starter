/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.email;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.email.mailtrap.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class MailtrapEmailFeature extends EmailFeature {

    public MailtrapEmailFeature(TemplateEmailFeature templateEmailFeature) {
        super(templateEmailFeature);
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Integration with Mailtrap to send transactional emails";
    }

    @Override
    public String getTitle() {
        return "Mailtrap Email";
    }

    @Override
    public String getModule() {
        return "mailtrap";
    }

    @Override
    @Nullable
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-email/2.11.0/guide/#mailtrap";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://mailtrap.io/";
    }
}
