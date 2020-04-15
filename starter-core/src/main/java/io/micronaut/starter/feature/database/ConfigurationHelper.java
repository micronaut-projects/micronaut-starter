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

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationHelper {

    public static final Map<String, Object> JDBC_H2;
    public static final Map<String, Object> JPA_DDL;

    static {
        final String prefix = "datasources.default.";
        JDBC_H2 = new LinkedHashMap<>();
        JDBC_H2.put(prefix + "url", "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
        JDBC_H2.put(prefix + "driverClassName", "org.h2.Driver");
        JDBC_H2.put(prefix + "username", "sa");
        JDBC_H2.put(prefix + "password", "");

        JPA_DDL = new LinkedHashMap<>();
        JPA_DDL.put("jpa.default.properties.hibernate.hbm2ddl.auto", "update");
    }
}
