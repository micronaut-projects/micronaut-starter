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
package io.micronaut.starter.cli.feature.acme;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.AccountBuilder;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.exception.AcmeException;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.security.KeyPair;

/**
 * Allows for creating a new account on the given acme server. Keypair can either be passed in or given as parameters.
 */
@Command(name = "create-acme-account",
        description = "Creates a new account on the given ACME server",
        usageHelpWidth = 95
)
@Prototype
public final class CreateAccount extends CreateKeyPair {
    @ReflectiveAccess
    @Option(names = {"-e", "--email"}, required = true, description = "Email address to create account with.")
    String email;

    @ReflectiveAccess
    @ArgGroup(multiplicity = "1", heading = "ACME server URL%n")
    AcmeServerOption acmeServerOption;

    public CreateAccount(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateAccount(CodeGenConfig config, ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier, ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("acme");
    }

    /**
     * Uses arguments passed to do all key creation and account creation.
     *
     * @return exit code of the program
     */
    public Integer call() {
        KeyPair accountKey;
        try {
            accountKey = doKeyCreation(keyDir, keyName, overwrite);
        } catch (IOException e) {
            err("Failed to create key at location : " + keyDir + ". Error: " + e.getMessage());
            return 1;
        }

        if (verbose()) {
            out("Opening session with " + acmeServerOption.serverUrl());
        }
        Session session = new Session(acmeServerOption.serverUrl());

        if (verbose()) {
            out("Creating account with key and email : " + email);
        }
        Account account;
        try {
            account = new AccountBuilder()
                    .addContact("mailto:" + email)
                    .agreeToTermsOfService()
                    .useKeyPair(accountKey)
                    .create(session);
        } catch (AcmeException e) {
            err("Failed to create account with key at : " + keyDir + "/" + keyName + ". Error: " + e.getMessage());
            return 1;
        }

        out("Account creation complete. Make sure to store your account pem somewhere safe as it is your only way to access your account.");
        if (verbose()) {
            out("Account url : " + account.getLocation());
            out("Account status : " + account.getStatus());
        }
        return 0;
    }
}
