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
package io.micronaut.starter.util;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.options.JdkVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VersionInfo {
    private static final Logger LOG = LoggerFactory.getLogger(VersionInfo.class);

    private static final Properties VERSIONS = new Properties();

    static {
        URL resource = VersionInfo.class.getResource("/micronaut-versions.properties");
        if (resource != null) {
            try (Reader reader = new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)) {
                VERSIONS.load(reader);
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * @return The starter version
     */
    public static String getStarterVersion() {
        Package aPackage = VersionInfo.class.getPackage();
        if (aPackage != null) {
            String implementationVersion = aPackage.getImplementationVersion();
            if (implementationVersion != null) {
                return implementationVersion;
            }
        }
        return getMicronautVersion();
    }

    /**
     * @return Checks whether the starter is a snapshot version.
     */
    public static boolean isStarterSnapshot() {
        return getStarterVersion().endsWith("-SNAPSHOT");
    }

    /**
     * @return Checks whether micronaut is a snapshot version.
     */
    public static boolean isMicronautSnapshot() {
        return getMicronautVersion().endsWith("-SNAPSHOT");
    }

    public static String getMicronautVersion() {
        Object micronautVersion = VERSIONS.get("micronaut.version");
        if (micronautVersion != null) {
            return micronautVersion.toString();
        }
        return "5.0.0";
    }

    public static Optional<Integer> getMicronautMajorVersion() {
        return getMajorVersion(getMicronautVersion());
    }

    public static Optional<Integer> getMajorVersion(String version) {
        String[] parts = version.split("\\.");
        if (parts.length >= 1) {
            try {
                return Optional.of(Integer.parseInt(parts[0]));
            } catch (NumberFormatException e) {
                LOG.error("could not parse major version of Micronaut", e);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the dependency versions.
     *
     * @return The versions
     */
    public static Map<String, String> getDependencyVersions() {
        Map<String, String> map = new LinkedHashMap<>();
        VERSIONS.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().toString()))
                .forEach(entry -> map.put(entry.getKey().toString(), entry.getValue().toString()));
        return Collections.unmodifiableMap(map);
    }

    /**
     * Get a dependency version from the BOM for the given ID.
     * @param id The ID
     * @return The dependency version as a string
     */
    public static @NonNull String getBomVersion(String id) {
        String key = id + ".version";
        Object version = VERSIONS.get(key);
        if (version != null) {
            return version.toString();
        }
        throw new IllegalArgumentException("Could not get version for ID " + id);
    }

    /**
     * Get a dependency version for the given ID.
     * @param id The ID
     * @return The dependency version
     */
    public static @NonNull Map.Entry<String, String> getDependencyVersion(String id) {
        String key = id + ".version";
        Object version = VERSIONS.get(key);
        if (version != null) {
            return new Map.Entry<String, String>() {
                @Override
                public String getKey() {
                    return key;
                }

                @Override
                public String getValue() {
                    return version.toString();
                }

                @Override
                public String setValue(String value) {
                    throw new UnsupportedOperationException("Cannot set version");
                }
            };
        }
        throw new IllegalArgumentException("Could not get version for ID " + id);
    }

    public static JdkVersion getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2);
        }
        // Allow these formats:
        // 1.8.0_72-ea
        // 9-ea
        // 9
        // 9.0.1
        int dotPos = version.indexOf('.');
        int dashPos = version.indexOf('-');
        return JdkVersion.valueOf(Integer.parseInt(version.substring(0,
                dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : version.length())));
    }

    public static String toJdkVersion(int javaVersion) {
        String jdkVersion = String.valueOf(javaVersion);
        return javaVersion <= 8 ? "1." + jdkVersion : jdkVersion;
    }

}
