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
import org.shredzone.acme4j.util.KeyPairUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;

import static picocli.CommandLine.Help.Visibility.ALWAYS;

/**
 * Allows for generating a keypair in a given location with a given size.
 * <p>
 * Alternative to using this is using openssl :
 * `openssl genrsa -out /tmp/mydomain.com-key.pem 4096`
 */
@Command(name = "create-key",
        description = "Creates an keypair for use with ACME integration"
)
@Prototype
public class CreateKeyPair extends CodeGenCommand {

    @ReflectiveAccess
    @Option(names = {"-k", "--key-dir"}, showDefaultValue = ALWAYS, defaultValue = "src/main/resources", description = "Custom location on disk to put the key to be used with this account.")
    String keyDir;

    @ReflectiveAccess
    @Option(names = {"-n", "--key-name"}, required = true, description = "Name of the key to be created")
    String keyName;

    @ReflectiveAccess
    @Option(names = {"-s", "--key-size"}, showDefaultValue = ALWAYS, defaultValue = "4096", description = "Size of the key to be generated")
    int keySize;

    public CreateKeyPair(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateKeyPair(CodeGenConfig config, ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier, ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("acme");
    }

    /**
     * Uses arguments passed to do all keypair creation.
     *
     * @return exit code of the program
     */
    public Integer call() {
        try {
            doKeyCreation(keyDir, keyName, keySize, overwrite);
            return 0;
        } catch (IOException e) {
            err("Failed to create key at location : " + keyDir + ". Error: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Create a keypair with a default size of 4096 bits.
     *
     * @param keyLocation output directory for key
     * @param keyName     name of keypair file
     * @return keypair
     * @throws IOException Failed to get/create keypair from disk
     */
    protected KeyPair doKeyCreation(String keyLocation, String keyName, boolean overwrite) throws IOException {
        return doKeyCreation(keyLocation, keyName, 4096, overwrite);
    }

    /**
     * Do the keypair creation.
     *
     * @param keyLocation Output location for the keypair
     * @param keyName     Name of the keypair file
     * @param keySize     Size of the keypair
     * @return KeyPair
     * @throws IOException Failed to get/create keypair from disk
     */
    private KeyPair doKeyCreation(String keyLocation, String keyName, int keySize, boolean overwrite) throws IOException {
        File keyDir = new File(keyLocation);
        if (!keyDir.exists()) {
            keyDir.mkdirs();
        }

        File keypairFile = new File(keyLocation, keyName);
        if (keypairFile.exists() && !overwrite) {
            out("Key already exists and can be found here : " + keypairFile + ". If you want to overwrite it use the -f/--force flag.");
            return KeyPairUtils.readKeyPair(new FileReader(keypairFile));
        } else {
            if (verbose()) {
                out("Creating key....");
            }
            KeyPair domainKey = KeyPairUtils.createKeyPair(keySize);

            if (verbose()) {
                out("Writing key to " + keypairFile + "....");
            }
            FileWriter fileWriter = new FileWriter(keypairFile);
            KeyPairUtils.writeKeyPair(domainKey, fileWriter);

            out("Key creation complete. It can be found here " + keypairFile + ".");
            return domainKey;
        }
    }
}

