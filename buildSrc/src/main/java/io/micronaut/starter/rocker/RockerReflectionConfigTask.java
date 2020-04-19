package io.micronaut.starter.rocker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gradle.api.file.RelativePath;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.*;

@CacheableTask
public class RockerReflectionConfigTask extends SourceTask {

    private File targetFile;

    public RockerReflectionConfigTask() {
        targetFile = new File(getProject().getBuildDir(), "classes/java/main/META-INF/native-image/io.micronaut.starter/micronaut-starter-core/reflect-config.json");
        source(getProject().fileTree("src/main/java"));
    }

    @OutputFile
    public File getTargetFile() {
        return targetFile;
    }

    @TaskAction
    public void assemble() throws IOException {
        String suffix = ".rocker.raw";
        List<Map<String, Object>> json = new ArrayList<>();
        getSource().visit(fileVisitDetails -> {
            RelativePath relativePath = fileVisitDetails.getRelativePath();
            if (relativePath.getLastName().endsWith(suffix)) {
                String[] segments = relativePath.getSegments();
                int last = segments.length - 1;
                segments[last] = segments[last].substring(0, segments[last].length() - suffix.length());
                String className = String.join(".", segments);
                Map<String, Object> classData = new HashMap<>(2);
                classData.put("name", className);
                classData.put("allDeclaredConstructors", true);
                json.add(classData);

                Map<String, Object> templateClass = new HashMap<>(2);
                templateClass.put("name", className + "$Template");
                Map<String, Object> templateConstructor = new HashMap<>(2);
                templateConstructor.put("name", "<init>");
                templateConstructor.put("parameterTypes", Collections.singletonList(className));
                templateClass.put("methods", Collections.singletonList(templateConstructor));
                json.add(templateClass);

            }
        });
        targetFile.getParentFile().mkdirs();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(targetFile, json);
    }
}
