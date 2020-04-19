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
import io.micronaut.starter.template.Template;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ZipOutputHandler implements OutputHandler {

    private final ZipArchiveOutputStream zipOutputStream;
    private final File zip;

    public ZipOutputHandler(Project project) throws IOException {
        File baseDirectory = new File(".").getCanonicalFile();
        Path zipPath = Paths.get(baseDirectory.getPath(), project.getName() + ".zip");
        zip = zipPath.toAbsolutePath().normalize().toFile();
        if (zip.exists()) {
            throw new IllegalArgumentException("Cannot create the project because the target zip file already exists");
        }
        zip.createNewFile();
        zipOutputStream = new ZipArchiveOutputStream(Files.newOutputStream(zip.toPath()));
    }

    public ZipOutputHandler(OutputStream outputStream) {
        zip = null;
        zipOutputStream = new ZipArchiveOutputStream(outputStream);
    }

    @Override
    public String getOutputLocation() {
        if (zip == null) {
            return null;
        } else {
            return zip.getAbsolutePath();
        }
    }

    @Override
    public boolean exists(String path) {
        return false;
    }

    @Override
    public void write(String path, Template contents) throws IOException {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(path);
        if (contents.isExecutable()) {
            zipEntry.setUnixMode(UnixStat.FILE_FLAG | 0755);
        }
        zipOutputStream.putArchiveEntry(zipEntry);
        contents.write(zipOutputStream);
        zipOutputStream.closeArchiveEntry();
    }

    @Override
    public void close() throws IOException {
        zipOutputStream.finish();
        zipOutputStream.close();
    }
}
