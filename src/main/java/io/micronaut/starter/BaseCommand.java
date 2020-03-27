package io.micronaut.starter;

import picocli.CommandLine;

public class BaseCommand {

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    @CommandLine.Mixin
    protected CommonOptionsMixin commonOptions = new CommonOptionsMixin();

    public void out(String message) {
        spec.commandLine().getOut().println(CommandLine.Help.Ansi.AUTO.string(message));
    }

    public void err(String message) {
        spec.commandLine().getErr().println(CommandLine.Help.Ansi.AUTO.string("@|bold,red | Error|@ " + message));
    }

    public void warning(String message) {
        spec.commandLine().getOut().println(CommandLine.Help.Ansi.AUTO.string("@|bold,red | Warning|@ " + message));
    }

    protected boolean showStacktrace() {
        return commonOptions.showStacktrace;
    }

    protected boolean verbose() {
        return commonOptions.verbose;
    }
}
