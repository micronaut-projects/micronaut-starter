package io.micronaut.starter.feature.picocli.test.junit;

import io.micronaut.starter.Project;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class PicocliJunit implements Feature {

    @Override
    public String getName() {
        return "picocli-junit";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("picocliJunitTest", getTemplate(commandContext.getLanguage(), commandContext.getProject()));
    }

    public RockerTemplate getTemplate(Language language, Project project) {
        if (language == Language.java) {
            return new RockerTemplate("src/test/java/{packagePath}/{className}CommandTest.java", picocliJunitTest.template(project));
        } else if (language == Language.groovy) {
            return new RockerTemplate("src/test/groovy/{packagePath}/{className}CommandSpec.groovy", picocliGroovyJunitTest.template(project));
        } else if (language == Language.kotlin) {
            return new RockerTemplate("src/test/kotlin/{packagePath}/{className}CommandSpec.kt", picocliKotlinJunitTest.template(project));
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(MicronautCommand command) {
        return command == MicronautCommand.CREATE_CLI;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
