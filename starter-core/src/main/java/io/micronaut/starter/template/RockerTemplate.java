/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.template;

import com.fizzed.rocker.RockerModel;
import com.fizzed.rocker.RockerOutput;

import java.io.OutputStream;

public class RockerTemplate extends DefaultTemplate {

    private final RockerWritable writable;

    private final boolean executable;

    public RockerTemplate(RockerModel delegate) {
        this(DEFAULT_MODULE, "", delegate, false);
    }

    public RockerTemplate(String path, RockerModel delegate) {
        this(DEFAULT_MODULE, path, delegate, false);
    }

    public RockerTemplate(String module, String path, RockerModel delegate) {
        this(module, path, delegate, false);
    }

    public RockerTemplate(String path, RockerModel delegate, boolean executable) {
        this(DEFAULT_MODULE, path, delegate, executable);
    }

    public RockerTemplate(String module, String path, RockerModel delegate, boolean executable) {
        super(module, path);
        this.writable = new RockerWritable(delegate);
        this.executable = executable;
    }

    @Override
    public void write(OutputStream outputStream) {
        writable.write(outputStream);
    }

    @Override
    public boolean isExecutable() {
        return executable;
    }

    public RockerWritable getWritable() {
        return writable;
    }

    public RockerOutput renderModel() {
        return writable.getModel().render();
    }
}
