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
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.AccountBuilder;
import org.shredzone.acme4j.Login;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.KeyPairUtils;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;

import static picocli.CommandLine.Help.Visibility.ALWAYS;

/**
 * Allows for deactivating of an existing Acme account on the given acme server.
 */
@Command(name = "deactivate-acme-account",
        description = "Deactivates an existing ACME account",
        usageHelpWidth = 95
)
@Prototype
public final class DeactivateAccount extends CodeGenCommand {
    @ReflectiveAccess
    @Option(names = {"-n", "--key-name"}, showDefaultValue = ALWAYS, description = "Name of the key to be used")
    String keyName;

    @ReflectiveAccess
    @Option(names = {"-k", "--key-dir"}, showDefaultValue = ALWAYS, defaultValue = "src/main/resources", description = "Directory to find the key to be used for this account.")
    String keyDir;

    @ReflectiveAccess
    @ArgGroup(multiplicity = "1", heading = "ACME server URL%n")
    AcmeServerOption acmeServerOption;

    public DeactivateAccount(@Parameter CodeGenConfig config) {
        super(config);
    }

    public DeactivateAccount(CodeGenConfig config, ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier, ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("acme");
    }

    /**
     * Uses arguments passed to do all account deactivation.
     *
     * @return exit code of the program
     */
    public Integer call() {
        File accountKeypairFile = new File(keyDir, keyName);
        if (accountKeypairFile.exists()) {
            if (verbose()) {
                out("Account keys exists, using it.");
            }
            KeyPair accountKey;
            try {
                accountKey = KeyPairUtils.readKeyPair(new FileReader(accountKeypairFile));
            } catch (IOException e) {
                err("Failed to read key at location : " + accountKeypairFile + ". Error: " + e.getMessage());
                return 1;
            }

            if (verbose()) {
                out("Opening session with " + acmeServerOption.serverUrl());
            }
            Session session = new Session(acmeServerOption.serverUrl());

            if (verbose()) {
                out("Logging in to account...");
            }
            Login login;
            try {
                login = new AccountBuilder()
                        .onlyExisting()
                        .useKeyPair(accountKey)
                        .createLogin(session);
            } catch (AcmeException e) {
                err("Failed to login to account using key : " + accountKeypairFile + ". Error: " + e.getMessage());
                return 1;
            }

            Account account = login.getAccount();
            try {
                account.deactivate();
            } catch (AcmeException e) {
                err("Failed to deactivate account using key : " + accountKeypairFile + ". Error: " + e.getMessage());
                return 1;
            }

            out("Account deactivation complete.");
            return 0;
        } else {
            err("ACCOUNT KEY IS REQUIRED AND WAS NOT FOUND AT : " + accountKeypairFile);
            return 1;
        }
    }
}
