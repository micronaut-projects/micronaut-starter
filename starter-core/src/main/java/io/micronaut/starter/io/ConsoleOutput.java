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
package io.micronaut.starter.io;

public interface ConsoleOutput {

    ConsoleOutput NOOP = new ConsoleOutput() {
        @Override
        public void out(String message) { }

        @Override
        public void err(String message) { }

        @Override
        public void warning(String message) { }

        @Override
        public boolean showStacktrace() {
            return false;
        }

        @Override
        public boolean verbose() {
            return false;
        }

    };

    void out(String message);

    void err(String message);

    void warning(String message);

    boolean showStacktrace();

    boolean verbose();

    default void green(String message) {
        out(message);
    }

    default void red(String message) {
        out(message);
    }
}
