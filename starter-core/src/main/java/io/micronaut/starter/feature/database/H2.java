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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Singleton;

@Singleton
public class H2 implements Feature {

    private static final Map<String, String> JDBC_CONFIG;

    static {
        String prefix = "datasources.default.";
        JDBC_CONFIG = new LinkedHashMap<>();
        JDBC_CONFIG.put(prefix + "url", "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
        JDBC_CONFIG.put(prefix + "driverClassName", "org.h2.Driver");
        JDBC_CONFIG.put(prefix + "username", "sa");
        JDBC_CONFIG.put(prefix + "password", "");
    }

    @Override
    public String getName() {
        return "h2";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().putAll(JDBC_CONFIG);
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

}
