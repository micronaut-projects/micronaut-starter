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

import io.micronaut.context.BeanContext
import io.micronaut.core.io.socket.SocketUtils
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.Testcontainers
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.utility.MountableFile
import spock.lang.AutoCleanup
import spock.lang.Shared

import java.time.Duration

abstract class CliBaseSpec extends CommandSpec implements CommandFixture {
    private static final Logger log = LoggerFactory.getLogger(CliBaseSpec.class)

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Shared
    int expectedHttpPort

    @Shared
    int expectedSecurePort

    @Shared
    int expectedPebbleServerPort

    @Shared
    GenericContainer certServerContainer

    @Shared
    String acmeServerUrl

    def setupSpec(){
        expectedHttpPort = SocketUtils.findAvailableTcpPort()
        expectedSecurePort = SocketUtils.findAvailableTcpPort()
        expectedPebbleServerPort = SocketUtils.findAvailableTcpPort()

        Testcontainers.exposeHostPorts(expectedHttpPort, expectedSecurePort)

        def file = File.createTempFile("pebble", "config")
        file.write """{
              "pebble": {
                "listenAddress": "0.0.0.0:${expectedPebbleServerPort}",
                "certificate": "test/certs/localhost/cert.pem",
                "privateKey": "test/certs/localhost/key.pem",
                "httpPort": $expectedHttpPort,
                "tlsPort": $expectedSecurePort
              }
            }"""

        log.info("Expected micronaut ports - http : {}, secure : {} ", expectedHttpPort, expectedSecurePort)
        log.info("Expected pebble config : {}", file.text)

        certServerContainer = new GenericContainer("letsencrypt/pebble:latest")
                .withCopyFileToContainer(MountableFile.forHostPath(file.toPath()), "/test/config/pebble-config.json")
                .withCommand("/usr/bin/pebble", "-strict", "false")
                .withEnv(getPebbleEnv())
                .withExposedPorts(expectedPebbleServerPort)
                .waitingFor(new WaitAllStrategy().withStrategy(new LogMessageWaitStrategy().withRegEx(".*ACME directory available.*\n"))
                        .withStrategy(new HostPortWaitStrategy())
                        .withStartupTimeout(Duration.ofMinutes(2)));
        certServerContainer.start()

        acmeServerUrl = "acme://pebble/${certServerContainer.containerIpAddress}:${certServerContainer.getMappedPort(expectedPebbleServerPort)}"
    }

    Map<String, String> getPebbleEnv(){
        return [
                "PEBBLE_VA_ALWAYS_VALID": "1"
        ]
    }
}
