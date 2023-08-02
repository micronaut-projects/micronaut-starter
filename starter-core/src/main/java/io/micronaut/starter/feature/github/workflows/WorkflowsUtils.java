/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.github.workflows;

import com.fizzed.rocker.RockerModel;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.feature.server.template.groovyController;
import io.micronaut.starter.feature.server.template.javaController;
import io.micronaut.starter.feature.server.template.kotlinController;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;

/**
 * Workflow utils.
 *
 * @author Pavol Gressa
 * @since 2.3
 */
public class WorkflowsUtils {

    /**
     * Resolves GraalVM jdk version based on the source version.
     *
     * @param javaVersion java version
     * @return graal vm version
     */
    public static String graalVersion(JdkVersion javaVersion) {
        return String.format("%s.java%s", VersionInfo.getDependencyVersion("graal").getValue(),
                javaVersion.majorVersion());
    }

    public static RockerTemplate createExampleController(Project project, Language language) {
        RockerModel model = null;
        switch (language) {
            case JAVA:
                model = javaController.template(project);
                break;
            case KOTLIN:
                model = kotlinController.template(project);
                break;
            case GROOVY:
                model = groovyController.template(project);
                break;
            case JAVA:
            default:
                model = javaController.template(project);
        }
        return new RockerTemplate(language.getSrcDir() + "/{packagePath}/{className}Controller." + language.getExtension(), model);
    }
}
