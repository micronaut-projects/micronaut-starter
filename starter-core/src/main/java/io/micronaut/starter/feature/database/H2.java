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

import io.micronaut.context.annotation.Primary;

import javax.inject.Singleton;

@Singleton
@Primary
public class H2 implements DatabaseDriverFeature {

    @Override
    public String getName() {
        return "h2";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE";
    }

    @Override
    public String getDriverClass() {
        return "org.h2.Driver";
    }

    @Override
    public String getDefaultUser() {
        return "sa";
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }

    @Override
    public String getDataDialect() {
        return "H2";
    }

}
