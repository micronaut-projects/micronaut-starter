package io.micronaut.starter;

import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

public class Options {

    private final Language language;
    private final TestFramework testFramework;
    private final BuildTool buildTool;

    public Options(Language language, TestFramework testFramework, BuildTool buildTool) {
        this.language = language;
        this.testFramework = testFramework;
        this.buildTool = buildTool;
    }

    public Language getLanguage() {
        return language;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public BuildTool getBuildTool() {
        return buildTool;
    }
}
