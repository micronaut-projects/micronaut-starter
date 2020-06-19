/*
 * Copyright 2017-2020 original authors
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
import org.shredzone.acme4j.Account
import org.shredzone.acme4j.AccountBuilder
import org.shredzone.acme4j.Session
import org.shredzone.acme4j.util.KeyPairUtils
import spock.lang.Shared

import java.nio.file.Files
import java.security.KeyPair

class DeactivateAccountSpec extends CliBaseSpec {

    @Shared
    File keyDir = Files.createTempDirectory('mn-acme-key-dir').toFile()

    void cleanupSpec() {
        keyDir.delete()
    }

    void "deactivate account when no key found"(){
        given:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        DeactivateAccount command = new DeactivateAccount(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.keyDir = keyDir.path
        command.keyName = "notfound.pem"

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 1

        and:
        1 * consoleOutput.err({ it.contains("ACCOUNT KEY IS REQUIRED AND WAS NOT FOUND") })
    }


    void "deactivate account when using keypair with no matching account"(){
        given:
        KeyPair keyPair = KeyPairUtils.createKeyPair(2048)
        def keyFile = new File(keyDir, "account.pem")
        KeyPairUtils.writeKeyPair(keyPair, keyFile.newWriter())

        and:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        DeactivateAccount command = new DeactivateAccount(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.keyDir = keyDir.path
        command.keyName = "account.pem"
        def option = new AcmeServerOption()
        option.serverUrl = acmeServerUrl
        command.acmeServerOption = option

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 1

        and:
        1 * consoleOutput.err("Failed to login to account using key : $keyFile.path. Error: unable to find existing account for only-return-existing request")
    }

    void "deactivate account happy path"(){
        given:
        KeyPair keyPair = KeyPairUtils.createKeyPair(2048)
        def keyFile = new File(keyDir, "account-happy-path.pem")
        KeyPairUtils.writeKeyPair(keyPair, keyFile.newWriter())

        and: "account already exists"
        Session session = new Session(acmeServerUrl);
        Account account = new AccountBuilder()
                    .addContact("mailto:testing@test.com")
                    .agreeToTermsOfService()
                    .useKeyPair(keyPair)
                    .create(session);

        and :
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        DeactivateAccount command = new DeactivateAccount(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.keyDir = keyDir.path
        command.keyName = "account-happy-path.pem"
        def option = new AcmeServerOption()
        option.serverUrl = acmeServerUrl
        command.acmeServerOption = option


        when:
        Integer exitCode = command.call()

        then:
        exitCode == 0

        and:
        1 * consoleOutput.out("Account deactivation complete.")
    }
}
