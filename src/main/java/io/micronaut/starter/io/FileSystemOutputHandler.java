package io.micronaut.starter.io;

import io.micronaut.starter.command.BaseCommand;
import io.micronaut.starter.OutputHandler;
import io.micronaut.starter.Project;
import io.micronaut.starter.template.URLTemplate;
import io.micronaut.starter.template.Template;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemOutputHandler implements OutputHandler {

    private final BaseCommand command;
    File applicationDirectory;

    public FileSystemOutputHandler(Project project, boolean inplace, BaseCommand command) throws IOException {
        this.command = command;
        File baseDirectory = new File(".").getCanonicalFile();
        if (inplace) {
            applicationDirectory = baseDirectory;
        } else {
            Path applicationPath = Paths.get(baseDirectory.getPath(), project.getAppName());
            applicationDirectory = applicationPath.toAbsolutePath().normalize().toFile();
        }
        if (applicationDirectory.exists() && !inplace) {
            throw new IllegalArgumentException("Cannot create the project because the target directory already exists");
        }
    }

    public FileSystemOutputHandler(File directory, BaseCommand command) throws IOException {
        this.command = command;
        this.applicationDirectory = directory;
    }

    @Override
    public void write(String path, Template contents) throws IOException {
        File targetFile = new File(applicationDirectory, path);
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (OutputStream os = Files.newOutputStream(targetFile.toPath())) {
            contents.write(os);
        }

        if (contents.isExecutable()) {
            if (!targetFile.setExecutable(true, true)) {
                command.warning("Failed to set " + path + " to be executable");
            }
        }
    }

    @Override
    public void close() throws IOException {

    }
}
