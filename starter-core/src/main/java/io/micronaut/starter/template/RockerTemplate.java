/*
 * Copyright 2017-2020 original authors
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

import java.io.OutputStream;

public class RockerTemplate implements Template {

    private final String path;
    private final RockerWritable writable;

    private final boolean executable;

    public RockerTemplate(String path, RockerModel delegate) {
        this(path, delegate, false);
    }

    public RockerTemplate(String path, RockerModel delegate, boolean executable) {
        this.path = path;
        this.writable = new RockerWritable(delegate);
        this.executable = executable;
    }

    @Override
    public void write(OutputStream outputStream) {
        writable.write(outputStream);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isExecutable() {
        return executable;
    }
}
