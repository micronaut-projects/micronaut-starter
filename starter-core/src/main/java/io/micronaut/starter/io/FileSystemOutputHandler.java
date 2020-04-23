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
package io.micronaut.starter.io;

import io.micronaut.starter.application.Project;
import io.micronaut.starter.template.Template;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemOutputHandler implements OutputHandler {

    File applicationDirectory;
    private final ConsoleOutput console;

    public FileSystemOutputHandler(Project project, boolean inplace, ConsoleOutput console) throws IOException {
        this.console = console;
        File baseDirectory = new File(".").getCanonicalFile();
        if (inplace) {
            applicationDirectory = baseDirectory;
        } else {
            Path applicationPath = Paths.get(baseDirectory.getPath(), project.getName());
            applicationDirectory = applicationPath.toAbsolutePath().normalize().toFile();
        }
        if (applicationDirectory.exists() && !inplace) {
            throw new IllegalArgumentException("Cannot create the project because the target directory already exists");
        }
    }

    public FileSystemOutputHandler(File directory, ConsoleOutput console) throws IOException {
        this.console = console;
        this.applicationDirectory = directory;
    }

    @Override
    public String getOutputLocation() {
        return applicationDirectory.getAbsolutePath();
    }

    @Override
    public boolean exists(String path) {
        return new File(applicationDirectory, path).exists();
    }

    @Override
    public void write(String path, Template contents) throws IOException {
        if ('/' != File.separatorChar) {
            path = path.replace('/', File.separatorChar);
        }
        File targetFile = new File(applicationDirectory, path);
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (OutputStream os = Files.newOutputStream(targetFile.toPath())) {
            contents.write(os);
        }

        if (contents.isExecutable()) {
            if (!targetFile.setExecutable(true, true)) {
                console.warning("Failed to set " + path + " to be executable");
            }
        }
    }

    @Override
    public void close() throws IOException {

    }
}
