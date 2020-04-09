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

import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.BaseCommand;
import io.micronaut.starter.template.Template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipOutputHandler implements OutputHandler {

    ZipOutputStream zipOutputStream;

    public ZipOutputHandler(Project project) throws IOException {
        File baseDirectory = new File(".").getCanonicalFile();
        Path zipPath = Paths.get(baseDirectory.getPath(), project.getName() + ".zip");
        File zip = zipPath.toAbsolutePath().normalize().toFile();
        if (zip.exists()) {
            throw new IllegalArgumentException("Cannot create the project because the target zip file already exists");
        }
        zip.createNewFile();
        zipOutputStream = new ZipOutputStream(Files.newOutputStream(zip.toPath()));
    }

    @Override
    public boolean exists(String path) {
        return false;
    }

    @Override
    public void write(String path, Template contents) throws IOException {
        ZipEntry zipEntry = new ZipEntry(path);
        zipOutputStream.putNextEntry(zipEntry);
        contents.write(zipOutputStream);
        zipOutputStream.closeEntry();
    }

    @Override
    public void close() throws IOException {
        zipOutputStream.finish();
        zipOutputStream.close();
    }
}
