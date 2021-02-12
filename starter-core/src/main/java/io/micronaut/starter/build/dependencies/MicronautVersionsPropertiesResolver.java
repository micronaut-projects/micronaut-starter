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
package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.util.VersionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Singleton
public class MicronautVersionsPropertiesResolver implements PropertiesResolver {
    private static final Logger LOG = LoggerFactory.getLogger(MicronautVersionsPropertiesResolver.class);
    public static final String MICRONAUT_VERSIONS_PROPERTIES = "/micronaut-versions.properties";
    private final Map<String, String> dependenciesVersions;

    public MicronautVersionsPropertiesResolver() {
        URL resource = VersionInfo.class.getResource(MICRONAUT_VERSIONS_PROPERTIES);
        Properties versions = new Properties();
        if (resource != null) {
            try (Reader reader = new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)) {
                versions.load(reader);
            } catch (IOException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("IO Exception reading {}", MICRONAUT_VERSIONS_PROPERTIES);
                }
            }

        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("resource {} not found", MICRONAUT_VERSIONS_PROPERTIES);
            }
        }
        this.dependenciesVersions = getDependencyVersions(versions);
    }

    @Override
    public Optional<String> resolve(@NonNull String key) {
        return Optional.ofNullable(dependenciesVersions.get(key));
    }

    /**
     * Gets the dependency versions.
     *
     * @return The versions
     */
    private static Map<String, String> getDependencyVersions(Properties versions) {
        Map<String, String> map = new LinkedHashMap<>();
        versions.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().toString()))
                .forEach((entry) -> map.put(entry.getKey().toString(), entry.getValue().toString()));
        return Collections.unmodifiableMap(map);
    }
}
