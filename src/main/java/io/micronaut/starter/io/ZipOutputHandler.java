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
    private final BaseCommand command;

    public ZipOutputHandler(Project project, BaseCommand command) throws IOException {
        this.command = command;
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
    public void write(String path, Template contents) throws IOException {
        ZipEntry zipEntry = new ZipEntry(path);
        zipOutputStream.putNextEntry(zipEntry);
        contents.write(zipOutputStream);
        zipOutputStream.closeEntry();
    }

    @Override
    public void close() throws IOException {
        zipOutputStream.close();
    }
}
