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
package io.micronaut.starter.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class VersionInfo {

    public static String getVersion() {
        URL resource = VersionInfo.class.getResource("/micronaut-version.properties");
        if (resource != null) {
            try(Reader reader = new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)) {
                Properties props = new Properties();
                props.load(reader);
                Object micronautVersion = props.get("micronautVersion");
                if (micronautVersion != null) {
                    return micronautVersion.toString();
                }
            } catch (IOException e) {
                // ignore
            }
        }
        return "2.0.0";
    }

    public static int getJavaVersion() {
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
        return Integer.parseInt(version.substring(0,
                dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : version.length()));
    }

    public static String toJdkVersion(int javaVersion) {
        String jdkVersion = String.valueOf(javaVersion);
        return javaVersion <= 8 ? "1." + jdkVersion : jdkVersion;
    }

}
