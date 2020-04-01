package io.micronaut.starter.feature.graalvm;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.graalvm.template.dockerBuildScript;
import io.micronaut.starter.feature.graalvm.template.dockerfile;
import io.micronaut.starter.feature.graalvm.template.nativeImageProperties;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;

@Singleton
public class GraalNativeImage implements Feature {

    @Override
    public String getName() {
        return "graal-native-image";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.addTemplate("dockerfile", new RockerTemplate("Dockerfile", dockerfile.template(commandContext.getProject())));
        commandContext.addTemplate("dockerBuildScript", new RockerTemplate("docker-build.sh", dockerBuildScript.template(commandContext.getProject()), true));

        commandContext.addTemplate("nativeImageProperties",
                new RockerTemplate("src/main/resources/META-INF/native-image/{packageName}/{appName}-application/native-image.properties",
                        nativeImageProperties.template(commandContext.getProject(), commandContext.getFeatures())
                )
        );
    }
}
