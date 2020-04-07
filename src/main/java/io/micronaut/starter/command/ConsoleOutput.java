package io.micronaut.starter.command;

import picocli.CommandLine;

public interface ConsoleOutput {

    void out(String message);

    void err(String message);

    void warning(String message);

    boolean showStacktrace();

    boolean verbose();
}
