/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        commandContext.addTemplate("dockerfile", new RockerTemplate("Dockerfile", dockerfile.template(commandContext.getProject(), commandContext.getBuildTool())));
        commandContext.addTemplate("dockerBuildScript", new RockerTemplate("docker-build.sh", dockerBuildScript.template(commandContext.getProject()), true));

        commandContext.addTemplate("nativeImageProperties",
                new RockerTemplate("src/main/resources/META-INF/native-image/{packageName}/{name}-application/native-image.properties",
                        nativeImageProperties.template(commandContext.getProject(), commandContext.getFeatures())
                )
        );
    }
}
