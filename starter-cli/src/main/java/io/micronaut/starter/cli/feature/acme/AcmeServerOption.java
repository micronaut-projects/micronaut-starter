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
package io.micronaut.starter.cli.feature.acme;

import picocli.CommandLine;

/**
 * Allows for helper flags to make selecting which ACME environment you are using easier.
 */
public final class AcmeServerOption {

    public static final String LE_PROD_URL = "https://acme-v02.api.letsencrypt.org/directory";

    public static final String LE_STAGING_URL = "https://acme-staging-v02.api.letsencrypt.org/directory";

    @CommandLine.Option(names = {"-u", "--url"}, required = true, description = "URL of ACME server to use")
    private String serverUrl;

    @CommandLine.Option(names = {"--lets-encrypt-prod"}, required = true, description = "Use the Let's Encrypt prod URL.")
    private boolean letsEncryptProd;

    @CommandLine.Option(names = {"--lets-encrypt-staging"}, required = true, description = "Use the Let's Encrypt staging URL")
    private boolean letsEncryptStaging;

    /**
     * Obtains the configured server url.
     *
     * @return ACME server url
     */
    public String serverUrl() {
        if (letsEncryptProd) {
            return LE_PROD_URL;
        }
        if (letsEncryptStaging) {
            return LE_STAGING_URL;
        }
        return serverUrl;

    }
}
