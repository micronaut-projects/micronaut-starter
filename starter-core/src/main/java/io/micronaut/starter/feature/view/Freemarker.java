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
package io.micronaut.starter.feature.view;

import javax.inject.Singleton;

@Singleton
public class Freemarker implements ViewFeature {

    @Override
    public String getName() {
        return "views-freemarker";
    }

    @Override
    public String getTitle() {
        return "Freemarker views";
    }

    @Override
    public String getDescription() {
        return "Adds support for Apache Freemarker as a Server Side View Rendering";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://freemarker.apache.org";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#freemarker";
    }
}
