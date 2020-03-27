package io.micronaut.starter.feature;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.template.BinaryTemplate;

public class KotlinTest implements TestFeature {

    @Override
    public String getName() {
        return "kotlinTest";
    }

    @Override
    public void apply(CommandContext commandContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        commandContext.addTemplate("kotlinTestConfig",
                new BinaryTemplate("src/test/kotlin/io/kotlintest/provided/ProjectConfig.kt",
                        classLoader.getResource("kotlintest/ProjectConfig.kt")));
    }
}
