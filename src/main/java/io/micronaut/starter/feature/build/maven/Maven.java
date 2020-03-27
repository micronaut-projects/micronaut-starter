package io.micronaut.starter.feature.build.maven;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.BuildFeature;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.maven.templates.pom;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;

public class Maven implements BuildFeature {

    private static final String WRAPPER_JAR = ".mvn/wrapper/maven-wrapper.jar";
    private static final String WRAPPER_PROPS = ".mvn/wrapper/maven-wrapper.properties";
    private static final String WRAPPER_DOWNLOADER = ".mvn/wrapper/MavenWrapperDownloader.java";

    @Override
    public String getName() {
        return "maven";
    }

    @Override
    public void apply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        commandContext.addTemplate("mavenWrapperJar", new BinaryTemplate(WRAPPER_JAR, classLoader.getResource("maven/" + WRAPPER_JAR)));
        commandContext.addTemplate("mavenWrapperProperties", new BinaryTemplate(WRAPPER_PROPS, classLoader.getResource("maven/" + WRAPPER_PROPS)));
        commandContext.addTemplate("mavenWrapperDownloader", new BinaryTemplate(WRAPPER_DOWNLOADER, classLoader.getResource("maven/" + WRAPPER_DOWNLOADER)));
        commandContext.addTemplate("mavenWrapper", new BinaryTemplate("mvnw", classLoader.getResource("maven/mvnw"), true));
        commandContext.addTemplate("mavenWrapperBat", new BinaryTemplate("mvnw.bat", classLoader.getResource("maven/mvnw.cmd"), true));

        commandContext.addTemplate("mavenPom", new RockerTemplate("pom.xml", pom.template(
                commandContext.getLanguage(),
                commandContext.getTestFramework(),
                commandContext.getProject(),
                commandContext.getFeatures(),
                commandContext.getProjectProperties()
        )));
        commandContext.addTemplate("gitignore", new RockerTemplate(".gitignore", gitignore.template()));
    }
}
