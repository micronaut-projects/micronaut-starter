/*
 * Copyright 2017-2022 original authors
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

package io.micronaut.starter.cli.feature.acme

import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.io.ConsoleOutput
import org.shredzone.acme4j.util.KeyPairUtils
import spock.lang.Shared

import java.nio.file.Files
import java.security.KeyPair

class CreateAccountCommandSpec extends CliBaseSpec {

    @Shared
    File keyDir = Files.createTempDirectory('mn-acme-key-dir').toFile()

    void cleanupSpec() {
        keyDir.delete()
    }

    void "create account when passing key details and the key doesn't exist"() {
        given:
        def keyFile = new File(keyDir, "create-account-will-do-this.pem")

        and: "we remove the key to make sure create account creates it"
        keyFile.delete()

        and:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateAccount command = new CreateAccount(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.keyDir = keyDir.path
        command.keyName = keyFile.name
        command.email = "testing@micronaut.com"
        def option = new AcmeServerOption()
        option.serverUrl = acmeServerUrl
        command.acmeServerOption = option

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 0

        and:
        1 * consoleOutput.out("Key creation complete. It can be found here " + keyFile.path + ".")
        1 * consoleOutput.out("Account creation complete. Make sure to store your account pem somewhere safe as it is your only way to access your account.")

        and:
        keyFile.exists()
        KeyPairUtils.readKeyPair(keyFile.newReader())
    }

    void "create account when key does exist"() {
        given:
        def keyFile = new File(keyDir, "pre-existing-key.pem")
        KeyPair keyPair = KeyPairUtils.createKeyPair(2048)
        KeyPairUtils.writeKeyPair(keyPair, keyFile.newWriter())

        and:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateAccount command = new CreateAccount(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.keyDir = keyDir.path
        command.keyName = keyFile.name
        command.email = "testing@micronaut.com"
        def option = new AcmeServerOption()
        option.serverUrl = acmeServerUrl
        command.acmeServerOption = option

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 0

        and:
        0 * consoleOutput.out("Key creation complete. It can be found here " + keyFile.path + ".")
        1 * consoleOutput.out("Account creation complete. Make sure to store your account pem somewhere safe as it is your only way to access your account.")
    }

    void "create account when no email passed"() {
        given:
        def keyFile = new File(keyDir, "doesntmatter.pem")

        and: "we remove the key to make sure create account creates it"
        keyFile.delete()

        and:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateAccount command = new CreateAccount(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.keyDir = keyDir.path
        command.keyName = keyFile.name
        def option = new AcmeServerOption()
        option.serverUrl = acmeServerUrl
        command.acmeServerOption = option

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 1

        and:
        1 * consoleOutput.err("Failed to create account with key at : " + keyFile.path + ". Error: contact email \"null\" is invalid")

        and:
        keyFile.exists()
        KeyPairUtils.readKeyPair(keyFile.newReader())
    }
}
