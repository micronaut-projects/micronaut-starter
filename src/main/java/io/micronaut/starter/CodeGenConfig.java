package io.micronaut.starter;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.List;

@Introspected
public class CodeGenConfig {

    private MicronautCommand command;
    private String defaultPackage;
    private TestFramework testFramework;
    private Language sourceLanguage;
    private List<String> features;

    public MicronautCommand getCommand() {
        return command;
    }

    public void setCommand(MicronautCommand command) {
        this.command = command;
    }

    public String getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public void setTestFramework(TestFramework testFramework) {
        this.testFramework = testFramework;
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
