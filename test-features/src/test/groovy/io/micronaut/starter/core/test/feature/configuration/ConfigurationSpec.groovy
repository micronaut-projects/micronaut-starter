package io.micronaut.starter.core.test.feature.configuration

import io.micronaut.starter.feature.config.Properties
import io.micronaut.starter.feature.config.Toml
import io.micronaut.starter.feature.config.Yaml
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.template.StringTemplate
import io.micronaut.starter.test.CommandSpec

class ConfigurationSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "configuration"
    }

    def 'test #buildTool configuration format #format'(BuildTool buildTool, String format) {
        when:
        generateProject(Language.JAVA, buildTool, format == Properties.NAME ? [] : [format])
        def fsoh = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fsoh.write('src/test/java/ConfigTest.java', new StringTemplate('src/test/java/ConfigTest.java', '''
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.ApplicationConfiguration;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigTest {
    @Test
    public void test() {
        ApplicationContext ctx = ApplicationContext.run();
        Assertions.assertEquals(ctx.getBean(ApplicationConfiguration.class).getName(), Optional.of("foo")); // 'foo' is configured in generateProject
    }
}
'''))
        def output = executeGradle("test")?.output

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [buildTool, format] << [BuildTool.valuesGradle(), [Toml.NAME, Yaml.NAME, Properties.NAME]].combinations()
    }
}
