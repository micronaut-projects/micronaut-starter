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
import org.shredzone.acme4j.util.KeyPairUtils
import spock.lang.Shared

import java.nio.file.Files
import java.security.KeyPair

class CreateKeyPairSpec extends CliBaseSpec {

    @Shared
    File keyDir = Files.createTempDirectory('mn-acme-key-dir').toFile()

    void cleanupSpec() {
        keyDir.delete()
    }

    void "create key with defaults"() {
        given:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateKeyPair command = new CreateKeyPair(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.keyDir = keyDir.path
        command.keyName = "new-keypair.pem"
        command.keySize = new Random().nextInt(1024) + 1024

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 0

        and:
        File keyFile = new File(keyDir, command.keyName)
        keyFile.exists()
        KeyPair pair = KeyPairUtils.readKeyPair(keyFile.newReader())
        pair.getPublic().getModulus().bitLength() == command.keySize
    }
}

