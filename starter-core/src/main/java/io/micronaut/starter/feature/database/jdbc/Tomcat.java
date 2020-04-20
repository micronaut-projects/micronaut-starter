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
package io.micronaut.starter.feature.database.jdbc;

import io.micronaut.context.annotation.Primary;
import io.micronaut.starter.feature.database.H2;

import javax.inject.Singleton;

@Singleton
@Primary
public class Tomcat extends JdbcFeature {

    public Tomcat(H2 h2) {
        super(h2);
    }

    @Override
    public String getName() {
        return "jdbc-tomcat";
    }

    @Override
    public String getTitle() {
        return "Tomcat JDBC Connection Pool";
    }

    @Override
    public String getDescription() {
        return "Configures SQL DataSource instances using Tomcat Connection Pool";
    }
}
