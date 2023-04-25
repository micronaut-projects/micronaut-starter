package io.micronaut.starter.io

import io.micronaut.starter.template.StringTemplate
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.nio.file.Files

class FileSystemOutputHandlerSpec extends Specification {

    void "test write template"() {
        when:
        File dir = new File(".");
        String path = "test.template";
        StringTemplate stringTemplate1 = new StringTemplate(path, "123");
        StringTemplate stringTemplate2 = new StringTemplate(path, "12");
        FileSystemOutputHandler  fileSystemOutputHandler = new FileSystemOutputHandler(dir, ConsoleOutput.NOOP)
        fileSystemOutputHandler.write("test.template", stringTemplate1);
        fileSystemOutputHandler.write("test.template", stringTemplate2);
        String result
        try (InputStream is = Files.newInputStream(new File(path).toPath())) {
            result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Files.delete(new File(path).toPath())
        }
        then:
        !result.contains("123");

    }
}
