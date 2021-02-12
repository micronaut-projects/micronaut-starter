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
package io.micronaut.starter.api;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.env.Environment;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

/**
 * Allows configuration of the the links exposed in URLs.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@ConfigurationProperties(StarterConfiguration.PREFIX)
public class StarterConfiguration {
    public static final String PREFIX = "micronaut.starter";

    private URL url;
    private String path;
    private URI redirectUri = URI.create("https://micronaut.io/launch");

    /**
     * Default constructor.
     */
    public StarterConfiguration() {
        String hostname = System.getenv(Environment.HOSTNAME);
        if (hostname != null) {
            try {
                this.url = new URL("https://" + hostname);
            } catch (MalformedURLException e) {
                // ignore
            }
        }
    }

    /**
     * @return The URI to redirect to when visiting via the browser
     */
    public Optional<URI> getRedirectUri() {
        return Optional.ofNullable(redirectUri);
    }

    /**
     * Sets the URI to redirect to when visiting via the browser.
     * @param redirectUri The redirect URI
     */
    public void setRedirectUri(@Nullable URI redirectUri) {
        this.redirectUri = redirectUri;
    }

    /**
     * @return The URL of the service
     */
    public Optional<URL> getUrl() {
        return Optional.ofNullable(url);
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * @return The path of the service.
     */
    public Optional<String> getPath() {
        return Optional.ofNullable(path);
    }

    public void setPath(String path) {
        this.path = path;
    }

    @ConfigurationProperties(GitHubConfiguration.PREFIX)
    public static class GitHubConfiguration {
        public static final String PREFIX = "github";

        private String clientId;
        private String clientSecret;
        private String tokenPermissions;

        /**
         * @return GitHub OAuth App client id
         */
        public String getClientId() {
            return clientId;
        }

        /**
         * Sets GitHub OAuth App client id
         * @param clientId client id
         */
        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        /**
         * @return GitHub OAuth App secret id
         */
        public String getClientSecret() {
            return clientSecret;
        }

        /**
         * Sets GitHub OAuth App secret
         * @param clientSecret secret
         */
        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        /**
         * @return token permission to request from user
         */
        public String getTokenPermissions() {
            return tokenPermissions;
        }

        /**
         * Sets GitHub token permissions
         * @param tokenPermissions token permissions
         */
        public void setTokenPermissions(String tokenPermissions) {
            this.tokenPermissions = tokenPermissions;
        }
    }
}
