package io.micronaut.starter.cli;

import org.gradle.api.internal.plugins.*;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.application.CreateStartScripts;
import org.gradle.util.TextUtil;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MicronautCreateStartScripts extends CreateStartScripts {

    @TaskAction
    public void generate() {
        StartScriptGenerator generator = new StartScriptGenerator(new DefaultTemplateBasedStartScriptGenerator(TextUtil.getUnixLineSeparator(), StartScriptTemplateBindingFactory.unix(), getProject().getResources().getText().fromFile("unixStartScript.txt")), new WindowsStartScriptGenerator());
        generator.setApplicationName(this.getApplicationName());
        generator.setMainClassName(this.getMainClassName());
        generator.setDefaultJvmOpts(this.getDefaultJvmOpts());
        generator.setOptsEnvironmentVar(this.getOptsEnvironmentVar());
        generator.setExitEnvironmentVar(this.getExitEnvironmentVar());
        generator.setClasspath(StreamSupport.stream(getProject().getTasks().getByName("shadowJar").getOutputs().getFiles().spliterator(), false).map(File::getName).collect(Collectors.toList()));
        generator.setScriptRelPath("bin/" + getUnixScript().getName());
        generator.generateUnixScript(this.getUnixScript());
        generator.generateWindowsScript(this.getWindowsScript());
    }
}
