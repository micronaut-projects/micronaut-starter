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
package io.micronaut.starter.template;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;

public abstract class DefaultTemplate implements Template {

    protected final String path;
    protected final String module;
    protected boolean useModule;

    protected DefaultTemplate(@NonNull String module, @NonNull String path) {
        this.module = module;
        this.path = path;
    }

    @Override
    public String getPath() {
        return useModule && StringUtils.isNotEmpty(module) ? String.join("/", module, path) : path;
    }

    @Override
    public void setUseModule(boolean useModule) {
        this.useModule = useModule;
    }

    @Override
    @NonNull
    public String getModule() {
        return module;
    }
}
