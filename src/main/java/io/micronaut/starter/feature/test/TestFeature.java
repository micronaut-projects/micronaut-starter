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
package io.micronaut.starter.feature.test;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.TestFramework;

import java.util.Map;

public interface TestFeature extends Feature {

    @Override
    default boolean isVisible() {
        return false;
    }

    @Override
    default int getOrder() {
        return FeaturePhase.TEST.getOrder();
    }

    @Override
    default void apply(CommandContext commandContext) {
        if (commandContext.getBuildTool() == BuildTool.maven) {
            Map<String, String> props = commandContext.getProjectProperties();
            props.put("maven-surefire-plugin.version", "2.22.2");
            props.put("maven-failsafe-plugin.version", "2.22.2");
        }
        doApply(commandContext);
    }

    void doApply(CommandContext commandContext);

    TestFramework getTestFramework();

    default boolean isJunit() {
        return getTestFramework() == TestFramework.junit;
    }

    default boolean isSpock() {
        return getTestFramework() == TestFramework.spock;
    }

    default boolean isKotlinTest() {
        return getTestFramework() == TestFramework.kotlintest;
    }
}
